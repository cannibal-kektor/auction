package kektor.auction.orchestrator.service;

import kektor.auction.orchestrator.dto.LotDto;
import kektor.auction.orchestrator.dto.BidRequestDto;
import kektor.auction.orchestrator.exception.ConcurrentSagaException;
import kektor.auction.orchestrator.log.LogHelper;
import kektor.auction.orchestrator.mapper.SagaMapper;
import kektor.auction.orchestrator.model.Saga;
import kektor.auction.orchestrator.model.SagaStatus;
import kektor.auction.orchestrator.repository.SagaRepository;
import kektor.auction.orchestrator.service.step.SagaStep;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

import static java.util.concurrent.CompletableFuture.allOf;
import static java.util.concurrent.CompletableFuture.runAsync;

@Slf4j
@Service
@RequiredArgsConstructor
public class SagaManager {

    final SagaRepository sagaRepository;
    final ObjectProvider<SagaStep> sagaSteps;
    final ThreadPoolTaskExecutor executor;
    final KafkaTemplate<?, Saga> kafkaTemplate;
    final SagaMapper mapper;
    final LogHelper logHelper;

    @Value("${app.kafka.stalled-saga}")
    String stalledSagaTopic;

    @Transactional
    public Saga prepareSaga(BidRequestDto bidRequestDto, LotDto lotDto) {
        Long lotId = lotDto.id();
        Instant createdOn = Instant.now();
        if (sagaRepository.existsByLotIdAndStatusAndCreatedOnIsBefore(
                lotId, SagaStatus.ACTIVE, createdOn))
            throw new ConcurrentSagaException();
        Saga saga = mapper.toModel(bidRequestDto, lotDto, createdOn);
        saga = sagaRepository.saveAndFlush(saga);
        return saga;
    }

    public void executeSaga(Saga saga) {
        List<SagaStep> steps = getSagaSteps(saga);
        executionPhase(steps, saga)
                .thenCompose(_ -> commitPhase(steps, saga)
                        .exceptionallyCompose(_ -> compensatePhase(steps, saga)))
                .exceptionallyCompose(_ -> compensatePhase(steps, saga));
    }

    CompletableFuture<Void> executionPhase(List<SagaStep> steps, Saga saga) {
        return allOf(steps.stream()
                .map(step -> runAsync(step::execute, executor)
                        .orTimeout(10, TimeUnit.SECONDS)
                        .exceptionally(step::handleExecutionException))
                .toArray(CompletableFuture<?>[]::new))
                .orTimeout(15, TimeUnit.SECONDS)
                .exceptionally(ex -> handleExecutionPhaseException(ex, saga));
    }

    CompletableFuture<Void> commitPhase(List<SagaStep> steps, Saga saga) {
        return allOf(steps.stream()
                .map(step -> runAsync(step::commit, executor)
                        .orTimeout(10, TimeUnit.SECONDS)
                        .exceptionally(step::handleCommitException))
                .toArray(CompletableFuture<?>[]::new))
                .orTimeout(15, TimeUnit.SECONDS)
                .thenApply(_ -> completeSaga(saga))
                .exceptionally(ex -> handleCommitPhaseException(ex, saga));
    }

    CompletableFuture<Void> compensatePhase(List<SagaStep> steps, Saga saga) {
        return compensatePhase(steps, saga, false);
    }

    void compensateStalledPhase(List<SagaStep> steps, Saga saga) {
        compensatePhase(steps, saga, true);
    }

    CompletableFuture<Void> compensatePhase(List<SagaStep> steps, Saga saga, boolean isStalled) {
        return allOf(steps.stream()
                .map(step -> runAsync(step::compensate, executor)
                        .orTimeout(10, TimeUnit.SECONDS)
                        .exceptionally(step::handleCompensateException))
                .toArray(CompletableFuture<?>[]::new))
                .orTimeout(15, TimeUnit.SECONDS)
                .thenApply(_ -> failSaga(saga))
                .exceptionally(ex -> {
                    if (!isStalled)
                        return handleCompensatePhaseException(ex, saga);
                    else
                        return handleStalledCompensatePhaseException(ex, saga);
                });
    }


    List<SagaStep> getSagaSteps(Saga saga) {
        return sagaSteps.stream()
                .map(step -> step.setSaga(saga))
                .toList();
    }

    public Void completeSaga(Saga saga) {
        saga.setStatus(SagaStatus.FINISHED);
        sagaRepository.save(saga);
        return null;
    }

    public Void failSaga(Saga saga) {
        saga.setStatus(SagaStatus.FAILED);
        sagaRepository.save(saga);
        return null;
    }

    @SneakyThrows
    public Void handleExecutionPhaseException(Throwable ex, Saga saga) {
        logHelper.logPhaseException(SagaPhase.EXECUTE, saga, ex);
        throw ex;
    }

    @SneakyThrows
    public Void handleCommitPhaseException(Throwable ex, Saga saga) {
        logHelper.logPhaseException(SagaPhase.COMMIT, saga, ex);
        throw ex;
    }

    public Void handleCompensatePhaseException(Throwable ex, Saga saga) {
        logHelper.logPhaseException(SagaPhase.COMPENSATE, saga, ex);
        saga.setStatus(SagaStatus.STALLED);
        rearrangeStalledCompensation(saga);
        sagaRepository.saveAndFlush(saga);
        return null;
    }

    @SneakyThrows
    public Void handleStalledCompensatePhaseException(Throwable ex, Saga saga) {
        Saga recheckSaga = sagaRepository.findById(saga.getSagaId())
                .orElseThrow();
        if (recheckSaga.getStatus() == SagaStatus.ACTIVE) {
            recheckSaga.setStatus(SagaStatus.STALLED);
            saga = sagaRepository.saveAndFlush(saga);
        }
        logHelper.logFailedAttemptResolvingStalledCompensation(saga, ex);
        throw ex;
    }

    public void rearrangeStalledCompensation(Saga saga) {
        kafkaTemplate.send(stalledSagaTopic, saga)
                .exceptionally(ex -> {
                    logHelper.logErrorForwardingStalledCompensation(saga, ex);
                    throw new RuntimeException(ex);
                });
    }

}

package kektor.auction.orchestrator.service;

import kektor.auction.orchestrator.dto.LotDto;
import kektor.auction.orchestrator.dto.NewBidRequestDto;
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
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClientResponseException;

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
    final BrokerService brokerService;
    final SagaMapper mapper;
    final LogHelper logHelper;

    @Transactional
    public Saga prepareSaga(NewBidRequestDto newBidRequestDto, LotDto lotDto, Instant createdOn) {
        Long lotId = lotDto.id();
        if (sagaRepository.existsByLotIdAndStatusAndCreatedOnIsBefore(
                lotId, SagaStatus.ACTIVE, createdOn))
            throw new ConcurrentSagaException(lotId, createdOn);
        Saga saga = mapper.toModel(newBidRequestDto, lotDto, createdOn);
        saga = sagaRepository.saveAndFlush(saga);
        return saga;
    }

    public void executeSaga(Saga saga) {
        List<SagaStep> steps = getSagaSteps(saga);
        executionPhase(steps, saga)
                .thenCompose(_ -> commitPhase(steps, saga)
                        .exceptionallyCompose(cause -> compensatePhase(steps, saga, cause)))
                .exceptionallyCompose(cause -> compensatePhase(steps, saga, cause));
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

    CompletableFuture<Saga> commitPhase(List<SagaStep> steps, Saga saga) {
        return allOf(steps.stream()
                .map(step -> runAsync(step::commit, executor)
                        .orTimeout(10, TimeUnit.SECONDS)
                        .exceptionally(step::handleCommitException))
                .toArray(CompletableFuture<?>[]::new))
                .orTimeout(15, TimeUnit.SECONDS)
                .thenApply(_ -> completeSaga(saga))
                .thenApply(brokerService::notifySagaStatusUpdate)
                .exceptionally(ex -> handleCommitPhaseException(ex, saga));
    }

    CompletableFuture<Saga> compensatePhase(List<SagaStep> steps, Saga saga, Throwable sagaFailCause, boolean isRetry) {
        return allOf(steps.stream()
                .map(step -> runAsync(step::compensate, executor)
                        .orTimeout(10, TimeUnit.SECONDS)
                        .exceptionally(step::handleCompensateException))
                .toArray(CompletableFuture<?>[]::new))
                .orTimeout(15, TimeUnit.SECONDS)
                .thenApply(_ -> failSaga(saga, sagaFailCause))
                .thenApply(brokerService::notifySagaStatusUpdate)
                .exceptionally(ex -> {
                    if (!isRetry)
                        return handleCompensatePhaseException(ex, saga);
                    else
                        return handleStalledCompensatePhaseException(ex, saga);
                });
    }

    CompletableFuture<Saga> compensatePhase(List<SagaStep> steps, Saga saga, Throwable sagaFailCause) {
        return compensatePhase(steps, saga, sagaFailCause, false);
    }

    void tryAbortedCompensation(List<SagaStep> steps, Saga saga) {
        compensatePhase(steps, saga, null, false);
    }

    void tryRetryStalledCompensation(List<SagaStep> steps, Saga saga) {
        compensatePhase(steps, saga, null, true);
    }


    List<SagaStep> getSagaSteps(Saga saga) {
        return sagaSteps.stream()
                .map(step -> step.setSaga(saga))
                .toList();
    }

    public Saga completeSaga(Saga saga) {
        saga.setStatus(SagaStatus.FINISHED);
        saga = sagaRepository.save(saga);
        return saga;
    }

    public Saga failSaga(Saga saga, Throwable sagaFailCause) {
        if (sagaFailCause instanceof RestClientResponseException e
                && e.getStatusCode() == HttpStatus.CONFLICT) {
            saga.setStatus(SagaStatus.CONCURRENT_REJECT);
        } else {
            saga.setStatus(SagaStatus.FAILED);
        }
        saga = sagaRepository.save(saga);
        return saga;
    }

    @SneakyThrows
    public Void handleExecutionPhaseException(Throwable ex, Saga saga) {
        logHelper.logPhaseException(SagaPhase.EXECUTE, saga, ex);
        throw ex;
    }

    @SneakyThrows
    public Saga handleCommitPhaseException(Throwable ex, Saga saga) {
        logHelper.logPhaseException(SagaPhase.COMMIT, saga, ex);
        throw ex;
    }

    public Saga handleCompensatePhaseException(Throwable ex, Saga saga) {
        logHelper.logPhaseException(SagaPhase.COMPENSATE, saga, ex);
        saga.setStatus(SagaStatus.STALLED);
        try {
            sagaRepository.save(saga);
        } catch (Exception e) {
            logHelper.logPhaseException(SagaPhase.COMPENSATE, saga, e);
        }
        brokerService.rearrangeStalledCompensation(saga);
        brokerService.notifySagaStatusUpdate(saga);
        return saga;
    }

    @SneakyThrows
    public Saga handleStalledCompensatePhaseException(Throwable ex, Saga saga) {
        logHelper.logFailedAttemptResolvingStalledCompensation(saga, ex);
        SagaStatus status = sagaRepository.findSagaStatusByLotId(saga.getSagaId());
        if (status == SagaStatus.ACTIVE) {
            saga.setStatus(SagaStatus.STALLED);
            sagaRepository.save(saga);
        }
        throw ex;
    }


}

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
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ProblemDetail;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClientResponseException;

import java.time.Instant;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.TimeUnit;

import static java.util.concurrent.CompletableFuture.allOf;
import static java.util.concurrent.CompletableFuture.runAsync;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.ProblemDetail.forStatusAndDetail;

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
    public Saga prepareSaga(BidRequestDto bidRequestDto, LotDto lotDto, Instant creationTime) {
        Long lotId = lotDto.id();
        Saga saga = mapper.toModel(bidRequestDto, lotDto, creationTime);
        try {
            saga = sagaRepository.saveAndFlush(saga);
        } catch (DataIntegrityViolationException e) {
            throw new ConcurrentSagaException(lotId, creationTime);
        }
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
                .exceptionally(ex -> handlePhaseException(SagaPhase.EXECUTE, ex, saga));
    }

    CompletableFuture<Void> commitPhase(List<SagaStep> steps, Saga saga) {
        return allOf(steps.stream()
                .map(step -> runAsync(step::commit, executor)
                        .orTimeout(10, TimeUnit.SECONDS)
                        .exceptionally(step::handleCommitException))
                .toArray(CompletableFuture<?>[]::new))
                .orTimeout(15, TimeUnit.SECONDS)
                .thenApply(_ -> completeSaga(saga))
                .thenApply(brokerService::notifySagaStatusUpdate)
                .exceptionally(ex -> handlePhaseException(SagaPhase.COMMIT, ex, saga));
    }

    CompletableFuture<Void> compensatePhase(List<SagaStep> steps, Saga saga, boolean isStalled) {
        return allOf(steps.stream()
                .map(step -> runAsync(step::compensate, executor)
                        .orTimeout(10, TimeUnit.SECONDS)
                        .exceptionally(step::handleCompensateException))
                .toArray(CompletableFuture<?>[]::new))
                .orTimeout(15, TimeUnit.SECONDS)
                .thenApply(_ -> failSaga(saga))
                .thenApply(brokerService::notifySagaStatusUpdate)
                .exceptionally(ex -> handleCompensatePhaseException(ex, saga, isStalled));
    }

    CompletableFuture<Void> compensatePhase(List<SagaStep> steps, Saga saga) {
        return compensatePhase(steps, saga, false);
    }

    void tryRetryStalledCompensation(List<SagaStep> steps, Saga saga) {
        compensatePhase(steps, saga, true);
    }

    List<SagaStep> getSagaSteps(Saga saga) {
        return sagaSteps.stream()
                .map(step -> step.setSaga(saga))
                .toList();
    }

    public Saga completeSaga(Saga saga) {
        saga.setStatus(SagaStatus.COMPLETED);
        return sagaRepository.save(saga);
    }

    public Saga failSaga(Saga saga) {
        saga.setStatus(SagaStatus.COMPENSATED);
        return sagaRepository.save(saga);
    }

    @SneakyThrows
    public Void handlePhaseException(SagaPhase phase, Throwable ex, Saga saga) {
        logHelper.logPhaseException(phase, saga, ex);

        ProblemDetail detail = null;
        if (ex instanceof CompletionException e) {
            if (e.getCause() instanceof RestClientResponseException restEx) {
                detail = restEx.getResponseBodyAs(ProblemDetail.class);
                if (detail == null) {
                    detail = forStatusAndDetail(restEx.getStatusCode(),
                            restEx.getResponseBodyAsString());
                }
            }
            ex = e.getCause();
        }

        if (detail == null) {
            detail = forStatusAndDetail(INTERNAL_SERVER_ERROR, ex.getMessage());
        }

        try {
            saga.setProblemDetail(detail);
            sagaRepository.save(saga);
        } catch (DataAccessException e) {
            logHelper.logPhaseException(phase, saga, e);
        }
        throw ex;
    }

    @SneakyThrows
    public Void handleCompensatePhaseException(Throwable ex, Saga saga, boolean isStalled) {
        if (isStalled) {
            logHelper.logFailedAttemptResolvingStalledCompensation(saga, ex);
            throw ex;
        }
        logHelper.logPhaseException(SagaPhase.COMPENSATE, saga, ex);
        saga.setStatus(SagaStatus.STALLED);
        try {
            sagaRepository.save(saga);
        } catch (Exception e) {
            logHelper.logPhaseException(SagaPhase.COMPENSATE, saga, e);
        }
        brokerService.rearrangeStalledCompensation(saga);
        brokerService.notifySagaStatusUpdate(saga);
        return null;
    }


}

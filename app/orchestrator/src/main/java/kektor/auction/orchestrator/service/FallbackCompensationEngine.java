package kektor.auction.orchestrator.service;

import kektor.auction.orchestrator.log.LogHelper;
import kektor.auction.orchestrator.model.Saga;
import kektor.auction.orchestrator.repository.SagaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.DltHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.RetryableTopic;
import org.springframework.kafka.retrytopic.SameIntervalTopicReuseStrategy;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.retry.annotation.Backoff;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Instant;

import static java.time.temporal.ChronoUnit.MINUTES;
import static kektor.auction.orchestrator.model.SagaStatus.ACTIVE;
import static org.springframework.kafka.retrytopic.RetryTopicHeaders.DEFAULT_HEADER_ATTEMPTS;

@Component
@RequiredArgsConstructor
public class FallbackCompensationEngine {

    final SagaManager sagaManager;
    final SagaRepository sagaRepository;
    final LogHelper logHelper;

    @RetryableTopic(attempts = "4", concurrency = "1",
            backoff = @Backoff(delay = 1000, multiplier = 2, maxDelay = 4000))
    @KafkaListener(topics = "${app.kafka.stalled-saga-topic}", groupId = "${spring.application.name}",
            clientIdPrefix = "${HOSTNAME}-stalled-saga-consumer", concurrency = "1")
    public void listenStalledSagasAndRetryCompensation(@Payload Saga stalledSaga,
                                                       @Header(name = DEFAULT_HEADER_ATTEMPTS, required = false) Integer attempt) {
        logHelper.logAttemptToResolveStalledSaga(stalledSaga, attempt);
        var steps = sagaManager.getSagaSteps(stalledSaga);
        sagaManager.tryRetryStalledCompensation(steps, stalledSaga);
    }

    @Scheduled(fixedRate = 10000)
    public void findAndAbortStuckSagas() {
        Instant pastLimit = Instant.now().minus(10, MINUTES);
        var stuckSagas = sagaRepository.findStalledByStatusAndCreatedOnIsBefore(ACTIVE, pastLimit);
        for (Saga saga : stuckSagas) {
            var steps = sagaManager.getSagaSteps(saga);
            sagaManager.tryAbortedCompensation(steps, saga);
        }
    }

    @DltHandler
    public void processDlt(@Payload(required = false) Saga saga,
                           @Header(KafkaHeaders.EXCEPTION_FQCN) String exName,
                           @Header(KafkaHeaders.EXCEPTION_MESSAGE) String exMessage) {
        logHelper.logManualInterventionMaybeRequired(saga, exName, exMessage);
    }


}

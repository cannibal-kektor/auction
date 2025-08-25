package kektor.auction.bid.service;

import kektor.auction.bid.dto.msg.SagaStatusMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.DltHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.RetryableTopic;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.retry.annotation.Backoff;
import org.springframework.stereotype.Service;


@Slf4j
@Service
@RequiredArgsConstructor
public class BrokerService {

    final PendingConfirmationService pendingConfirmationService;

    @RetryableTopic(attempts = "4", concurrency = "1",
            backoff = @Backoff(delay = 1000, multiplier = 2, maxDelay = 4000))
    @KafkaListener(topics = "${app.kafka.saga-status-topic}", groupId = "${HOSTNAME}",
            clientIdPrefix = "${HOSTNAME}-saga-events-consumer", concurrency = "2")
    public void listenSagaEvents(SagaStatusMessage msg) {
        pendingConfirmationService.notifyWaitingClient(msg);
    }

    @DltHandler
    public void handleDlt(@Payload(required = false) SagaStatusMessage msg,
//                          ConsumerRecord<String, SagaStatusMessage> record,
                          @Header(KafkaHeaders.DLT_EXCEPTION_FQCN) String exceptionName,
                          @Header(KafkaHeaders.DLT_EXCEPTION_MESSAGE) String exceptionMessage) {

        log.error("DLT record received [SagaStatusMessage] : exName={}, exMessage={}, value={}"
                , exceptionName, exceptionMessage, msg);

    }

}

package kektor.auction.sse.service;

import kektor.auction.sse.dto.LotUpdateMessage;
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

    final SseManager sseManager;

    @RetryableTopic(attempts = "4", concurrency = "1",
            backoff = @Backoff(delay = 1000, multiplier = 2, maxDelay = 4000))
    @KafkaListener(topics = "${app.kafka.lot.update-event-topic}", groupId = "${HOSTNAME}",
            clientIdPrefix = "${HOSTNAME}-lot-update-events-consumer", concurrency = "2")
    public void listenSagaEvents(LotUpdateMessage msg) {
        sseManager.notifySseEmitters(msg);
    }

    @DltHandler
    public void handleDlt(@Payload(required = false) LotUpdateMessage msg,
//                          ConsumerRecord<String, LotUpdateMessage> record,
                          @Header(KafkaHeaders.DLT_EXCEPTION_FQCN) String exceptionName,
                          @Header(KafkaHeaders.DLT_EXCEPTION_MESSAGE) String exceptionMessage) {
        log.error("DLT record received [LotUpdateMessage] : exName={}, exMessage={}, value={}"
                , exceptionName, exceptionMessage, msg);

    }

}

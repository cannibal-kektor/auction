package kektor.auction.bid.service;

import kektor.auction.bid.dto.BidEventMessage;
import kektor.auction.lot.dto.CategoryEventMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import java.util.List;


@Slf4j
@Service
@RequiredArgsConstructor
public class BrokerEventService {

    final SagaOrchestratorService sagaOrchestratorService;

    final KafkaTemplate<Long, BidEventMessage> kafkaTemplate;

    @Value("${app.kafka.placed-bid}")
    String placedBidTopic;

    @Value("${app.kafka.category.event-topic}")
    String categoryEventTopic;

    @Async
    @TransactionalEventListener(classes = BidEventMessage.class,
            phase = TransactionPhase.AFTER_COMMIT)
    public void newCategoryEvent(BidEventMessage event) {
        kafkaTemplate.send(placedBidTopic, event.bidDto().lotId(), event)
                .exceptionally(ex -> {
                            log.error("Error while sending bid event", ex);
                            return null;
                        });
    }

    @KafkaListener(topics = "${app.kafka.category.event-topic}", groupId = "${HOSTNAME}",
            clientIdPrefix = "${HOSTNAME}", concurrency = "1")
    public void listenCategoryEvents(@Payload CategoryEventMessage message,
                                     @Header(KafkaHeaders.RECEIVED_KEY) long categoryId) {
        switch (message.eventType()) {
            case UPDATED -> sagaOrchestratorService.evictCategoryById(categoryId);
            case DELETED -> {
                sagaOrchestratorService.evictCategoryById(categoryId);
                sagaOrchestratorService.triggerDeleteStale(List.of(categoryId));
            }
        }
    }

//    @KafkaListener(id="validated", topics = "annotated35", errorHandler = "validationErrorHandler",
//            containerFactory = "kafkaJsonListenerContainerFactory")
//    public void validatedListener(@Payload @Valid ValidatedClass val) {
//    ...
//    }
//
//    @Bean
//    public KafkaListenerErrorHandler validationErrorHandler() {
//        return (m, e) -> {
//        ...
//        };

}

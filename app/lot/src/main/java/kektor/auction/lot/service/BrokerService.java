package kektor.auction.lot.service;


import kektor.auction.lot.dto.msg.CategoryEventMessage;
import kektor.auction.lot.dto.msg.LotUpdateMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class BrokerService {

    final KafkaTemplate<Long, LotUpdateMessage> kafkaTemplate;
    final CategoryCacheService categoryCacheService;
    final LotService lotService;

    @Value("${app.kafka.lot.update-event-topic}")
    String lotUpdateEventTopic;


    @Async
    @TransactionalEventListener(
            classes = LotUpdateMessage.class,
            phase = TransactionPhase.AFTER_COMMIT)
    public void lotChanged(LotUpdateMessage eventMsg) {
        kafkaTemplate.send(lotUpdateEventTopic, eventMsg.lotId(), eventMsg)
                .exceptionally(ex -> {
                    log.error("Error while sending lot update event", ex);
                    return null;
                });
    }

    @KafkaListener(topics = "${app.kafka.category.update-event-topic}", groupId = "${HOSTNAME}",
            clientIdPrefix = "${HOSTNAME}-category-events-consumer", concurrency = "2")
    public void listenCategoryEvents(@Payload CategoryEventMessage message,
                                     @Header(KafkaHeaders.RECEIVED_KEY) long categoryId) {
        switch (message.eventType()) {
            case UPDATED -> categoryCacheService.evictCategoryById(categoryId);
            case DELETED -> {
                categoryCacheService.evictCategoryById(categoryId);
                categoryCacheService.triggerDeleteStale(List.of(categoryId));
            }
        }
        lotService.notifyLotUpdateSubscribers(categoryId, message);
    }


}

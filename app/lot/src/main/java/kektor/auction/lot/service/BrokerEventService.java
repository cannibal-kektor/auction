package kektor.auction.lot.service;

import kektor.auction.lot.dto.CategoryEventMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
public class BrokerEventService {

    final CategoryCacheService categoryCacheService;

    @KafkaListener(topics = "${app.kafka.category.event-topic}", groupId = "${HOSTNAME}",
            clientIdPrefix = "${HOSTNAME}", concurrency = "1")
    public void listenCategoryEvents(@Payload CategoryEventMessage message,
                                     @Header(KafkaHeaders.RECEIVED_KEY) long categoryId) {
        switch (message.eventType()) {
            case UPDATED -> categoryCacheService.evictCategoryById(categoryId);
            case DELETED -> {
                categoryCacheService.evictCategoryById(categoryId);
                categoryCacheService.triggerDeleteStale(List.of(categoryId));
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

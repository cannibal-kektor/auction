package kektor.auction.category.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;
import kektor.auction.category.dto.CategoryEventMessage;

@Slf4j
@Service
@RequiredArgsConstructor
public class BrokerEventService {

    final KafkaTemplate<Long, CategoryEventMessage> kafkaTemplate;

    @Value("${app.kafka.category.event-topic}")
    String categoryEventTopic;

    @Async
    @TransactionalEventListener(classes = CategoryEventMessage.class,
            phase = TransactionPhase.AFTER_COMMIT)
    public void newCategoryEvent(CategoryEventMessage event) {
        kafkaTemplate.send(categoryEventTopic, event.categoryDto().id(), event)
                .exceptionally(
                        ex -> {
                            log.error("Error while sending category event", ex);
                            return null;
                        });
    }

}

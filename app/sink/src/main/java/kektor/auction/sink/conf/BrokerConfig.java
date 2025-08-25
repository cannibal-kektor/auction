package kektor.auction.sink.conf;

import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.serialization.ByteArraySerializer;
import org.apache.kafka.common.serialization.Serializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ContainerCustomizer;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.CommonErrorHandler;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.kafka.listener.DeadLetterPublishingRecoverer;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.kafka.support.serializer.DelegatingByTypeSerializer;
import org.springframework.kafka.support.serializer.JsonSerializer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.util.backoff.FixedBackOff;

import java.util.LinkedHashMap;
import java.util.function.BiFunction;

@Configuration
public class BrokerConfig {

    @Value("${app.kafka.cdc.dlt-topic}")
    String cdcDltTopic;

    @Bean
    public NewTopic cdcDltTopic() {
        return TopicBuilder.name(cdcDltTopic)
                .partitions(1)
                .replicas(1)
                .build();
    }

    @Bean
    CommonErrorHandler brokerDefaultErrorHandler(DeadLetterPublishingRecoverer recoverer) {
        return new DefaultErrorHandler(recoverer, new FixedBackOff(1000L, 3L));
    }

    @Bean
    DeadLetterPublishingRecoverer defaultRecoverer(KafkaTemplate<?, ?> kafkaTemplate) {
        BiFunction<ConsumerRecord<?, ?>, Exception, TopicPartition> destinationResolver =
                (cr, e) -> new TopicPartition(cdcDltTopic, -1);
        return new DeadLetterPublishingRecoverer(kafkaTemplate, destinationResolver);
    }

    @Bean
    ContainerCustomizer<?, ?, ConcurrentMessageListenerContainer<Object, Object>> kafkaContainerCustomizer(ThreadPoolTaskExecutor executor) {
        return container ->
                container.getContainerProperties().setListenerTaskExecutor(executor);
    }

    public static class CustomDelegatingByTypeSerializer extends DelegatingByTypeSerializer {

        static LinkedHashMap<Class<?>, Serializer<?>> delegatingMap = new LinkedHashMap<>();

        static {
            delegatingMap.put(byte[].class, new ByteArraySerializer());
            delegatingMap.put(Object.class, new JsonSerializer<>());
        }

        public CustomDelegatingByTypeSerializer() {
            super(delegatingMap, true);
        }
    }

}

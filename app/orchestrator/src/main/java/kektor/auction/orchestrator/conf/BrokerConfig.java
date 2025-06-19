package kektor.auction.orchestrator.conf;

import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.common.serialization.ByteArraySerializer;
import org.apache.kafka.common.serialization.Serializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafkaRetryTopic;
import org.springframework.kafka.config.ContainerCustomizer;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.kafka.support.serializer.DelegatingByTypeSerializer;
import org.springframework.kafka.support.serializer.JsonSerializer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.LinkedHashMap;

@EnableKafkaRetryTopic
@Configuration
public class BrokerConfig {

    @Value("${app.kafka.stalled-saga}")
    String stalledSagaCompensation;

    @Value("${app.kafka.saga-status-topic}")
    String sagaStatusTopic;

    @Bean
    public NewTopic stalledSagaCompensationTopic() {
        return TopicBuilder.name(stalledSagaCompensation)
                .partitions(1)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic sagaStatusTopic() {
        return TopicBuilder.name(sagaStatusTopic)
                .partitions(1)
                .replicas(1)
                .build();
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

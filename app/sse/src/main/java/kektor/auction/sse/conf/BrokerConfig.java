package kektor.auction.sse.conf;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafkaRetryTopic;
import org.springframework.kafka.config.ContainerCustomizer;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.listener.CommonErrorHandler;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.util.backoff.FixedBackOff;

@EnableKafkaRetryTopic
@Configuration
public class BrokerConfig {

    @Value("${app.kafka.lot.update-event-topic}")
    String lotUpdateEventTopic;

    @Bean
    public NewTopic lotUpdateEventTopic() {
        return TopicBuilder.name(lotUpdateEventTopic)
                .partitions(1)
                .replicas(1)
                .build();
    }

    @Bean
    ContainerCustomizer<?, ?, ConcurrentMessageListenerContainer<Object, Object>> kafkaContainerCustomizer(ThreadPoolTaskExecutor executor) {
        return container ->
                container.getContainerProperties().setListenerTaskExecutor(executor);
    }

    @Bean
    CommonErrorHandler brokerDefaultErrorHandler() {
        return new DefaultErrorHandler(new FixedBackOff(500L, 3L));
    }

}

package kektor.auction.category.conf;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class BrokerConfig {

    @Value("${app.kafka.category.update-event-topic}")
    String categoryEventTopic;

    @Bean
    public NewTopic categoryEventTopic() {
        return TopicBuilder.name(categoryEventTopic)
                .partitions(1)
                .replicas(1)
                .build();
    }
    
}

package kektor.auction.orchestrator.conf;

import org.apache.kafka.clients.CommonClientConfigs;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.RoundRobinPartitioner;
import org.apache.kafka.common.config.SslConfigs;
import org.apache.kafka.common.serialization.ByteArraySerializer;
import org.apache.kafka.common.serialization.LongDeserializer;
import org.apache.kafka.common.serialization.LongSerializer;
import org.apache.kafka.common.serialization.Serializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.kafka.ConcurrentKafkaListenerContainerFactoryConfigurer;
import org.springframework.boot.autoconfigure.kafka.KafkaConnectionDetails;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.boot.autoconfigure.kafka.SslBundleSslEngineFactory;
import org.springframework.boot.ssl.SslBundle;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafkaRetryTopic;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.support.serializer.DelegatingByTypeSerializer;
import org.springframework.kafka.support.serializer.ErrorHandlingDeserializer;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.util.StringUtils;

import java.util.LinkedHashMap;
import java.util.Map;

import static java.util.Map.entry;

@EnableKafkaRetryTopic
@Configuration
public class BrokerConf {

    @Value("${app.kafka.stalled-saga}")
    String stalledSagaCompensation;

    @Value("${app.kafka.saga-status-topic}")
    String sagaStatusTopic;

    @Bean
    public NewTopic stalledSagaCompensationTopic() {
        return TopicBuilder.name(stalledSagaCompensation)
                .partitions(1)
                .replicas(1)
//                .compact()
                .build();
    }

    @Bean
    public NewTopic sagaStatusTopic() {
        return TopicBuilder.name(sagaStatusTopic)
                .partitions(1)
                .replicas(1)
//                .compact()
                .build();
    }

    @Bean
    ConcurrentKafkaListenerContainerFactory<?, ?> kafkaListenerContainerFactory(
            ConcurrentKafkaListenerContainerFactoryConfigurer configurer,
            ConsumerFactory<Object, Object> kafkaConsumerFactory,
            ThreadPoolTaskExecutor executor) {
        var factory = new ConcurrentKafkaListenerContainerFactory<>();
        configurer.configure(factory, kafkaConsumerFactory);
//        kafkaContainerCustomizer.ifAvailable(factory::setContainerCustomizer);
        factory.getContainerProperties().setListenerTaskExecutor(executor);
//        factory.setConcurrency();
//        factory.setCommonErrorHandler();
        return factory;
    }

    @Bean
    DefaultKafkaConsumerFactory<?, ?> kafkaConsumerFactory(KafkaProperties kafkaProperties,
                                                           KafkaConnectionDetails connectionDetails) {
        var properties = kafkaProperties.buildConsumerProperties();
        var consumerSettings = connectionDetails.getConsumer();
        properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, consumerSettings.getBootstrapServers());
        applySecurityProtocol(properties, connectionDetails.getSecurityProtocol());
        applySslBundle(properties, consumerSettings.getSslBundle());
        properties.putAll(customConsumerProperties());
        return new DefaultKafkaConsumerFactory<>(properties);
    }

    @Bean
    DefaultKafkaProducerFactory<?, ?> kafkaProducerFactory(KafkaProperties kafkaProperties,
                                                           KafkaConnectionDetails connectionDetails) {
        var properties = kafkaProperties.buildProducerProperties();
        var producerSettings = connectionDetails.getProducer();
        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, producerSettings.getBootstrapServers());
        applySecurityProtocol(properties, producerSettings.getSecurityProtocol());
        applySslBundle(properties, producerSettings.getSslBundle());
        properties.putAll(customProducerProperties());
        var factory = new DefaultKafkaProducerFactory<>(properties);
        String transactionIdPrefix = kafkaProperties.getProducer().getTransactionIdPrefix();
        if (transactionIdPrefix != null) {
            factory.setTransactionIdPrefix(transactionIdPrefix);
        }
        return factory;
    }

    void applySslBundle(Map<String, Object> properties, SslBundle sslBundle) {
        if (sslBundle != null) {
            properties.put(SslConfigs.SSL_ENGINE_FACTORY_CLASS_CONFIG, SslBundleSslEngineFactory.class);
            properties.put(SslBundle.class.getName(), sslBundle);
        }
    }

    void applySecurityProtocol(Map<String, Object> properties, String securityProtocol) {
        if (StringUtils.hasLength(securityProtocol)) {
            properties.put(CommonClientConfigs.SECURITY_PROTOCOL_CONFIG, securityProtocol);
        }
    }

    Map<String, Object> customProducerProperties() {
        return Map.ofEntries(
                entry(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, LongSerializer.class),
                entry(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, CustomDelegatingByTypeSerializer.class),
//                entry(ProducerConfig.PARTITIONER_CLASS_CONFIG, RoundRobinPartitioner.class.getName()),
                entry(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, true),
                entry(ProducerConfig.RETRIES_CONFIG, Integer.MAX_VALUE),
                entry(ProducerConfig.DELIVERY_TIMEOUT_MS_CONFIG, 60000),
                entry(JsonSerializer.TYPE_MAPPINGS, "sagaMessage:kektor.auction.orchestrator.model.Saga,sagaStatus:kektor.auction.orchestrator.dto.msg.SagaStatusMessage")
//                entry(config.put(ProducerConfig.MAX_IN_FLIGHT_REQUESTS_PER_CONNECTION, 5);)
        );
    }

    Map<String, Object> customConsumerProperties() {
        return Map.ofEntries(
                entry(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, ErrorHandlingDeserializer.class),
                entry(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, ErrorHandlingDeserializer.class),
                entry(ErrorHandlingDeserializer.KEY_DESERIALIZER_CLASS, LongDeserializer.class),
                entry(ErrorHandlingDeserializer.VALUE_DESERIALIZER_CLASS, JsonDeserializer.class.getName()),
                entry(JsonDeserializer.TYPE_MAPPINGS, "sagaMessage:kektor.auction.orchestrator.model.Saga")
//                entry(JsonDeserializer.USE_TYPE_INFO_HEADERS , false)
//                entry(ErrorHandlingDeserializer.VALIDATOR_CLASS, ),
//                entry(JsonDeserializer.KEY_DEFAULT_TYPE, "com.example.MyKey"),
//                entry(JsonDeserializer.VALUE_DEFAULT_TYPE, "com.example.MyValue"),
//                entry(JsonDeserializer.TRUSTED_PACKAGES, "com.example"),
        );
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

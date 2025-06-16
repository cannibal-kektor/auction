package kektor.auction.bid.conf;

import org.apache.kafka.clients.CommonClientConfigs;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
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
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.CommonErrorHandler;
import org.springframework.kafka.listener.DeadLetterPublishingRecoverer;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.kafka.support.serializer.DelegatingByTypeSerializer;
import org.springframework.kafka.support.serializer.ErrorHandlingDeserializer;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.util.StringUtils;
import org.springframework.util.backoff.FixedBackOff;

import java.util.LinkedHashMap;
import java.util.Map;

import static java.util.Map.entry;

@Configuration
public class BrokerConf {

    @Value("${app.kafka.category.event-topic}")
    String categoryEventTopic;

    @Bean
    public NewTopic categoryEventTopic() {
        return TopicBuilder.name(categoryEventTopic)
                .partitions(1)
                .replicas(1)
//                .compact()
                .build();
    }

    @Bean
    public NewTopic categoryEventTopicDLT() {
        return TopicBuilder.name(categoryEventTopic + "-dlt")
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
                entry(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, CustomDelegatingByTypeSerializer.class)
        );
    }

    Map<String, Object> customConsumerProperties() {
        return Map.ofEntries(
                entry(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, ErrorHandlingDeserializer.class),
                entry(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, ErrorHandlingDeserializer.class),
                entry(ErrorHandlingDeserializer.KEY_DESERIALIZER_CLASS, LongDeserializer.class),
                entry(ErrorHandlingDeserializer.VALUE_DESERIALIZER_CLASS, JsonDeserializer.class.getName()),
                entry(JsonDeserializer.TYPE_MAPPINGS, "categoryEvent:kektor.auction.lot.dto.CategoryEventMessage")
//                entry(JsonDeserializer.USE_TYPE_INFO_HEADERS , false)
//                entry(ErrorHandlingDeserializer.VALIDATOR_CLASS, ),
//                entry(JsonDeserializer.KEY_DEFAULT_TYPE, "com.example.MyKey"),
//                entry(JsonDeserializer.VALUE_DEFAULT_TYPE, "com.example.MyValue"),
//                entry(JsonDeserializer.TRUSTED_PACKAGES, "com.example"),
        );
    }

//    @Bean
//    DefaultKafkaConsumerFactoryCustomizer kafkaConsumerFactoryCustomizer() {
//        return consumerFactory -> {
//
//            var props = consumerFactory.getConfigurationProperties();
//            props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, ErrorHandlingDeserializer.class);
//            props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, ErrorHandlingDeserializer.class);
//            props.put(ErrorHandlingDeserializer.KEY_DESERIALIZER_CLASS, LongDeserializer.class);
//            props.put(ErrorHandlingDeserializer.VALUE_DESERIALIZER_CLASS, JsonDeserializer.class.getName());

    /// /            props.put(ErrorHandlingDeserializer.VALIDATOR_CLASS, );
    /// /            props.put(JsonDeserializer.KEY_DEFAULT_TYPE, "com.example.MyKey");
    /// /            props.put(JsonDeserializer.VALUE_DEFAULT_TYPE, "com.example.MyValue")
    /// /            props.put(JsonDeserializer.TRUSTED_PACKAGES, "com.example");
//        };
//    }

//    @Bean
//    DefaultKafkaProducerFactoryCustomizer kafkaProducerFactoryCustomizer() {
//        return producerFactory -> {
//            var props = producerFactory.getConfigurationProperties();
//            props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, LongSerializer.class);
//            props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, CustomDelegatingByTypeSerializer.class);
//        };
//
//    }
    @Bean
    CommonErrorHandler brokerDefaultErrorHandler(DeadLetterPublishingRecoverer recoverer) {
        return new DefaultErrorHandler(recoverer, new FixedBackOff(1000L, 3L));
    }

    @Bean
    DeadLetterPublishingRecoverer defaultRecoverer(KafkaTemplate<?, ?> kafkaTemplate) {
//        BiFunction<ConsumerRecord<?, ?>, Exception, TopicPartition> destinationResolver=
//        ew DeadLetterPublishingRecoverer(kafkaTemplate, destinationResolver)
        return new DeadLetterPublishingRecoverer(kafkaTemplate);
    }

//    @Override
//    protected Consumer<DeadLetterPublishingRecovererFactory> configureDeadLetterPublishingContainerFactory() {
//        return dlprf -> dlprf.setPartitionResolver((cr, nextTopic) -> null);
//    }


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

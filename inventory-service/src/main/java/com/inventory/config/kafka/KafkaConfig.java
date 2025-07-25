package com.inventory.config.kafka;

import io.micronaut.context.annotation.Bean;
import io.micronaut.context.annotation.Factory;
import io.micronaut.context.annotation.Value;
import jakarta.inject.Singleton;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.HashMap;
import java.util.Map;

@Factory
@RequiredArgsConstructor
public class KafkaConfig {

    private static final int PARTITIONS_COUNT = 2;
    private static final int REPLICA_COUNT = 1;

    @Value("${kafka.bootstrap.servers}")
    private String bootstrapServers;
    @Value("${kafka.consumer.group-id}")
    private String groupId;
    @Value("${kafka.consumer.auto-offset-reset}")
    private String autoOffsetReset;
    @Value("${kafka.topic.orchestrator}")
    private String orchestratorTopic;
    @Value("${kafka.topic.inventory-success}")
    private String inventorySuccessTopic;
    @Value("${kafka.topic.inventory-fail}")
    private String inventoryFailTopic;

    @Singleton
    @Bean
    public KafkaConsumer<String, String> kafkaConsumer() {
        return new KafkaConsumer<>(consumerProps());
    }

    private Map<String, Object> consumerProps() {
        Map<String,Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, autoOffsetReset);
        return props;
    }

    @Bean
    @Singleton
    public KafkaProducer<String, String> kafkaProducer() {
        return new KafkaProducer<>(producerProps());
    }

    private Map<String, Object> producerProps() {
        Map<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        return props;
    }

    private NewTopic buildTopic(String name){
        return new NewTopic(name, PARTITIONS_COUNT, (short) REPLICA_COUNT);
    }
    @Bean
    public NewTopic orchestratorTopic(){
        return buildTopic(orchestratorTopic);
    }
    @Bean
    public NewTopic inventorySuccessTopic(){
        return buildTopic(inventorySuccessTopic);
    }
    @Bean
    public NewTopic inventoryFailTopic(){
        return buildTopic(inventoryFailTopic);
    }
}

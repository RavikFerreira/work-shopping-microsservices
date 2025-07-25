package com.shopping.core.kafka;

import io.micronaut.context.annotation.Value;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@Singleton
public class Producer {

    private static final Logger LOG = LoggerFactory.getLogger(Producer.class);

    @Inject
    private KafkaProducer kafkaProducer;

    @Value("${kafka.topic.start}")
    private String startTopic;

    public void sendEvent(String payload){
        try {
            LOG.info("Sending event to topic {} with data {}", startTopic, payload);
            int partition = 1;
            String key = "1";
            ProducerRecord<Object, Object> record = new ProducerRecord<>(startTopic, partition, key,  payload);
            kafkaProducer.send(record);
        } catch (Exception e) {
            LOG.error("Error trying to send data to topic {} with data {}", startTopic, payload, e);
        }
    }
    public void sendEventProduct(String payload){
        try {
            LOG.info("Sending event product to topic {} with data {}", startTopic, payload);
            int partition = 0;
            String key = "2";
            ProducerRecord<Object, Object> record = new ProducerRecord<>(startTopic, partition, key, payload);
            kafkaProducer.send(record);
        } catch (Exception e) {
            LOG.error("Error trying to send data to topic {} with data {}", startTopic,  payload, e);
        }
    }
}

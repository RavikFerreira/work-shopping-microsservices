package com.example.core.kafka;

import com.example.core.dto.Event;
import com.example.core.dto.EventProduct;
import com.example.core.services.OrchestratorProductService;
import com.example.core.services.OrchestratorService;
import com.example.core.utils.JsonUtil;
import io.micronaut.configuration.kafka.annotation.*;
import jakarta.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

@KafkaListener(groupId = "${kafka.consumer.group-id}")
public class Consumer {

    private static final Logger LOG = LoggerFactory.getLogger(Consumer.class);
    @Inject
    private OrchestratorService orchestratorService;
    @Inject
    private JsonUtil jsonUtil;
    @Inject
    private OrchestratorProductService orchestratorProductService;

    @Topic("${kafka.topic.start}")
    public void consumerStartEvent(@KafkaKey String key, String payload) {
        if (Objects.equals(key, "1")) {
            LOG.info("Receiving event {} from start topic", payload);
            Event event = jsonUtil.toEvent(payload);
            orchestratorService.start(event);
        }
        if (Objects.equals(key, "2")) {
            LOG.info("Receiving event {} from start product topic", payload);
            EventProduct event = jsonUtil.toEventProduct(payload);
            orchestratorProductService.start(event);
        }
    }

    @Topic("${kafka.topic.orchestrator}")
    public void consumerOrchestratorEvent(@KafkaKey String key, String payload) {
        if (Objects.equals(key, "1")) {
            LOG.info("Receiving event {} from orchestrator topic", payload);
            Event event = jsonUtil.toEvent(payload);
            orchestratorService.continueSaga(event);
        }
        if (Objects.equals(key, "2")) {
            LOG.info("Receiving event product {} from orchestrator topic", payload);
            EventProduct event = jsonUtil.toEventProduct(payload);
            orchestratorProductService.continueSaga(event);
        }
    }

    @Topic("${kafka.topic.finish-success}")
    public void consumerFinishSuccessEvent(@KafkaKey String key, String payload) {
        if (Objects.equals(key, "1")) {
            LOG.info("Receiving event {} from finish-success topic", payload);
            Event event = jsonUtil.toEvent(payload);
            orchestratorService.finishSuccess(event);
        }
        if (Objects.equals(key, "2")) {
            LOG.info("Receiving event product {} from finish-success topic",payload);
            EventProduct event = jsonUtil.toEventProduct(payload);
            orchestratorProductService.finishSuccess(event);
        }
    }
    @Topic("${kafka.topic.finish-fail}")
    public void consumerFinishFailEvent(@KafkaKey String key, String payload){
        if (Objects.equals(key, "1")) {
            LOG.info("Receiving ending notification event {} from finish-fail topic", payload);
            Event event = jsonUtil.toEvent(payload);
            orchestratorService.finishFail(event);
        }
        if (Objects.equals(key, "2")) {
            LOG.info("Receiving ending notification event product {} from finish-fail topic" , payload);
            EventProduct event = jsonUtil.toEventProduct(payload);
            orchestratorProductService.finishFail(event);
        }
    }
}

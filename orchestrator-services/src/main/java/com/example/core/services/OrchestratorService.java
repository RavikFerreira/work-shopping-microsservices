package com.example.core.services;

import com.example.core.dto.Event;
import com.example.core.dto.History;
import com.example.core.enums.EEventSource;
import com.example.core.enums.EStatus;
import com.example.core.enums.ETopic;
import com.example.core.kafka.Producer;
import com.example.core.saga.SagaExecutionController;
import com.example.core.utils.JsonUtil;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;

import static com.example.core.enums.ETopic.NOTIFY;

@Singleton
@AllArgsConstructor
public class OrchestratorService {

    private static final Logger LOG = LoggerFactory.getLogger(OrchestratorService.class);

    @Inject
    private JsonUtil jsonUtil;
    @Inject
    private Producer producer;
    @Inject
    private SagaExecutionController sagaExecutionController;

    public void start(Event event){
        event.setSource(EEventSource.ORCHESTRATOR);
        event.setStatus(EStatus.SUCCESS);
        ETopic topic = getTopic(event);
        LOG.info("STARTED!");
        addHistory(event, "Started!");
        sendToProducerWithTopic(event,topic);
    }
    public void finishSuccess(Event event){
        event.setSource(EEventSource.ORCHESTRATOR);
        event.setStatus(EStatus.SUCCESS);
        LOG.info("FINISHED SUCCESSFULLY FOR EVENT {}", event.getId());
        addHistory(event, "Finished successfully!");
        notifyFinished(event);
    }
    public void finishFail(Event event){
        event.setSource(EEventSource.ORCHESTRATOR);
        event.setStatus(EStatus.FAIL);
        LOG.info("FINISHED WITH ERRORS FOR EVENT {}", event.getId());
        addHistory(event, "Finished with errors!");
        notifyFinished(event);
    }
    public void continueSaga(Event event) {
        ETopic topic = getTopic(event);
        LOG.info("SAGA CONTINUING FOR EVENT {}", event.getId());
        sendToProducerWithTopic(event,topic);
    }

    private ETopic getTopic (Event event){
        return sagaExecutionController.getNextTopic(event);
    }

    private void addHistory(Event event, String message){
        History history = new History();
        history.setSource(String.valueOf(event.getSource()));
        history.setStatus(String.valueOf(event.getStatus()));
        history.setMessage(message);
        history.setCreatedAt(LocalDateTime.now());
        event.addToHistory(history);
    }

    private void sendToProducerWithTopic(Event event, ETopic topic){
        producer.sendEvent(jsonUtil.toJson(event), topic.getTopic());
    }

    private void notifyFinished(Event event){
        producer.sendEvent(jsonUtil.toJson(event), NOTIFY.getTopic());
    }
}

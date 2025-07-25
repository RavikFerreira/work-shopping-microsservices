package com.example.core.saga;

import com.example.core.dto.Event;
import com.example.core.enums.EEventSource;
import com.example.core.enums.ETopic;
import io.micronaut.http.annotation.Controller;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;

import static com.example.core.saga.Handler.EVENT_SOURCE_INDEX;
import static com.example.core.saga.Handler.HANDLER;
import static com.example.core.saga.Handler.STATUS_INDEX;
import static com.example.core.saga.Handler.TOPIC_INDEX;
import static java.lang.String.format;


@Controller
@AllArgsConstructor
public class SagaExecutionController {
    private static final Logger LOG = LoggerFactory.getLogger(SagaExecutionController.class);
    private static final String LOG_ID = "SHOPPING ID: %s | TRANSACTION ID %s | EVENT ID %s";

    public ETopic getNextTopic(Event event) {
        if (event.getSource() == null || event.getStatus() == null) {
            throw new RuntimeException("Source and status must be informed.");
        }
        var topic = findTopicBySourceAndStatus(event);
        logCurrentSaga(event, topic);
        return topic;
    }

    public ETopic findTopicBySourceAndStatus(Event event){
        return (ETopic) Arrays.stream(HANDLER).filter(row -> isEventSourceAndStatusValid(event, row))
                .map(i -> i[TOPIC_INDEX])
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Topic not found!"));
    }

    private boolean isEventSourceAndStatusValid(Event event, Object[] row){
        var source = row[EVENT_SOURCE_INDEX];
        var status = row[STATUS_INDEX];
        return source.equals(event.getSource()) && status.equals(event.getStatus());
    }
    private void logCurrentSaga(Event event, ETopic topic){
        var sagaId = createSagaId(event);
        EEventSource source = event.getSource();
        switch (event.getStatus()){
            case SUCCESS -> LOG.info("### CURRENT SAGA: {} | SUCCESS | NEXT TOPIC {} | {}"
                    , source, topic, sagaId);
            case ROLLBACK_PENDING -> LOG.info("### CURRENT SAGA: {} | SENDING TO ROLLBACK CURRENT SERVICE | NEXT TOPIC {} | {}"
                    , source, topic, sagaId);
            case FAIL -> LOG.info("### CURRENT SAGA: {} | SENDING TO ROLLBACK PREVIOUS SERVICE | NEXT TOPIC {} | {}"
                    , source, topic, sagaId);
        }
    }

    private String createSagaId(Event event){
        return format(LOG_ID, event.getPayload().getId(), event.getTransactionId(), event.getId());
    }
}

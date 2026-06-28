package com.shopping.core.kafka;


import com.shopping.core.service.ShoppingService;
import com.shopping.core.utils.JsonUtil;
import io.micronaut.configuration.kafka.annotation.KafkaListener;
import io.micronaut.configuration.kafka.annotation.Topic;
import jakarta.inject.Inject;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@AllArgsConstructor
@KafkaListener(groupId = "${kafka.consumer.group-id}")
public class UserConsumer {
    private static final Logger LOG = LoggerFactory.getLogger(UserConsumer.class);

    @Inject
    private ShoppingService shoppingService;
    @Inject
    private JsonUtil jsonUtil;

    @Topic("${kafka.topic.login}")
    public void consumerLoginEvent(String payload){
        LOG.info("Receiving ending notification event {} from notify topic" , payload);
        var event = jsonUtil.toUserEvent(payload);
        shoppingService.addShoppingCart(event);
    }
}

package com.shopping.core.service;

import com.shopping.config.exceptions.ShoppingResourceNotFoundException;
import com.shopping.core.dto.EventFilters;
import com.shopping.core.models.Event;
import com.shopping.core.models.EventProduct;
import com.shopping.core.repository.EventProductRepository;
import com.shopping.core.repository.EventRepository;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.List;

import static io.micronaut.core.util.StringUtils.isEmpty;

@Singleton
public class EventService {
    private static final Logger LOG = LoggerFactory.getLogger(EventService.class);

    @Inject
    private EventRepository eventRepository;
    @Inject
    private EventProductRepository eventProductRepository;

    public void notify(Event event){
        event.setShoppingId(event.getShoppingId());
        event.setCreatedAt(LocalDateTime.now());
        save(event);
        LOG.info("ShoppingID {} with notified! TransactionID: {} " + event.getShoppingId(),  event.getTransactionId());
    }
    public List<Event> findAll(){
        return eventRepository.findAllOrderByCreatedAtDesc();
    }

    private Event findByShoppingId(String shoppingId){
        return eventRepository.findTop1ByShoppingIdOrderByCreatedAtDesc(shoppingId)
                .orElseThrow(() -> new RuntimeException("Event not found by shoppingID."));
    }


    public Event findByFilters(String shoppingId){
        validateEmptyFilters(shoppingId);
        if(!isEmpty(shoppingId)){
            return findByShoppingId(shoppingId);
        }
        else{
            throw new ShoppingResourceNotFoundException("Shopping not found");
        }
    }

    private void validateEmptyFilters(String shoppingId){
        EventFilters filters = new EventFilters();
        filters.setProductId(shoppingId);
        if(isEmpty(filters.getProductId())){
            throw new RuntimeException("ShoppingId must be informed.");
        }
    }

    public void save(Event event){
        eventRepository.save(event);
    }


    public void save(EventProduct event){
        eventProductRepository.save(event);
    }

}


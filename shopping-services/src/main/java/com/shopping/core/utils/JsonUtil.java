package com.shopping.core.utils;

import com.shopping.core.models.Event;
import com.shopping.core.models.EventProduct;
import io.micronaut.serde.ObjectMapper;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import lombok.AllArgsConstructor;

@Singleton
@AllArgsConstructor
public class JsonUtil {

    @Inject
    private ObjectMapper objectMapper;

    public String toJson(Object object){
        try{
            return objectMapper.writeValueAsString(object);
        } catch (Exception e){
            return "";
        }
    }

    public Event toEvent(String json){
        try{
            return objectMapper.readValue(json, Event.class);
        } catch (Exception e){
            return null;
        }
    }
//    public EventProduct toEventProduct(String json){
//        try{
//            return objectMapper.readValue(json, EventProduct.class);
//        } catch (Exception e){
//            return null;
//        }
//    }
}

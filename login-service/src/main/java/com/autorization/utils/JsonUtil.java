package com.autorization.utils;

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
}

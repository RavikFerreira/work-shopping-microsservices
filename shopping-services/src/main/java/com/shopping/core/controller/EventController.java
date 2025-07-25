package com.shopping.core.controller;

import com.shopping.core.models.Event;
import com.shopping.core.service.EventService;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.QueryValue;
import jakarta.inject.Inject;

import java.util.List;

@Controller("event/")
public class EventController {

    @Inject
    private EventService eventService;

    @Get("filters/{shoppingId}")
    public Event findByFilters(@QueryValue String shoppingId){
        return eventService.findByFilters(shoppingId);
    }

    @Get("list")
    public List<Event> findAll(){
        return eventService.findAll();
    }
}

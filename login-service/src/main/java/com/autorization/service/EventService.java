package com.autorization.service;

import com.autorization.models.entity.UserEvent;
import com.autorization.repostory.EventRepository;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;


@Singleton
public class EventService {
    @Inject
    private EventRepository eventRepository;

    public void save(UserEvent event){
        eventRepository.save(event);
    }
}

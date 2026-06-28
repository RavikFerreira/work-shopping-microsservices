package com.autorization.models.entity;

import io.micronaut.serde.annotation.Serdeable;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

@Serdeable
@Entity
public class UserEvent {

    @Id
    @GeneratedValue
    private long id;

    public UserEvent() {
    }

    public UserEvent(Long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}

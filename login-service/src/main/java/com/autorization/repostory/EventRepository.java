package com.autorization.repostory;

import com.autorization.models.entity.UserEvent;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.repository.CrudRepository;

@Repository
public interface EventRepository extends CrudRepository<UserEvent, Long> {
}

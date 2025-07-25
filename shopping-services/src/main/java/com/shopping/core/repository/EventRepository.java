package com.shopping.core.repository;

import com.shopping.core.models.Event;
import io.micronaut.data.mongodb.annotation.MongoRepository;
import io.micronaut.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

@MongoRepository(databaseName = "shopping-db")
public interface EventRepository extends CrudRepository<Event, String> {

    Optional<Event> findTop1ByShoppingIdOrderByCreatedAtDesc(String productId);
    Optional<Event> findTop1ByTransactionIdOrderByCreatedAtDesc(String transactionId);

    List<Event> findAllOrderByCreatedAtDesc();
}

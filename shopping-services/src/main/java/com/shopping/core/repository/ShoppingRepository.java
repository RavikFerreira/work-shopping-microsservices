package com.shopping.core.repository;

import com.shopping.core.models.Shopping;
import io.micronaut.data.mongodb.annotation.MongoRepository;
import io.micronaut.data.repository.CrudRepository;

import java.util.Optional;

@MongoRepository(databaseName = "shopping-db")
public interface ShoppingRepository extends CrudRepository<Shopping, String> {

    Optional<Shopping> findByIdShopping(String idShopping);
}

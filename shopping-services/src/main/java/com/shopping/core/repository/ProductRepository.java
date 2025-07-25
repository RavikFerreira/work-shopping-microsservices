package com.shopping.core.repository;

import com.shopping.core.models.Product;
import io.micronaut.data.mongodb.annotation.MongoRepository;
import io.micronaut.data.repository.CrudRepository;

import java.util.Optional;

@MongoRepository(databaseName = "shopping-db")
public interface ProductRepository extends CrudRepository<Product, String> {

    Optional<Product> findByIdProduct (String idProduct);

}

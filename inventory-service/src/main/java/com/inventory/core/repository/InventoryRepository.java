package com.inventory.core.repository;

import com.inventory.core.models.Inventory;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InventoryRepository extends JpaRepository<Inventory, Long> {

    Optional<Inventory> findByIdProduct(String idProduct);
    Boolean existsByShoppingIdAndTransactionId(String shoppingId, String transactionId);
    List<Inventory> findByShoppingIdAndTransactionId(String shoppingId, String transactionId);
    Boolean existsByIdProduct(String IdProduct);
}

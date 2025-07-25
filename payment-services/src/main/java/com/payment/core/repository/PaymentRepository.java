package com.payment.core.repository;

import com.payment.core.models.Payment;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.jpa.repository.JpaRepository;

import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {

    boolean existsByShoppingIdAndTransactionId(String shoppingId, String transactionId);
    Optional<Payment> findByShoppingIdAndTransactionId(String shoppingId, String transactionId);
}

package com.shopping.core.dto;

import io.micronaut.serde.annotation.Serdeable;
import lombok.AllArgsConstructor;

@Serdeable
@AllArgsConstructor
public class EventFilters {

    private String productId;
    private String transactionId;

    public EventFilters() {
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }
}

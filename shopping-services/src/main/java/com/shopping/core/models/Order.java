package com.shopping.core.models;

import io.micronaut.serde.annotation.Serdeable;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@Serdeable
public class Order implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private String idOrder;
    private List<Product> products;

    public Order(String idOrder, List<Product> products) {
        this.idOrder = idOrder;
        this.products = products;
    }
    public Order() {}

    public String getIdOrder() {
        return idOrder;
    }

    public void setIdOrder(String idOrder) {
        this.idOrder = idOrder;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

}

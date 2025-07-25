package com.productvalidationservice.core.dto;

import io.micronaut.serde.annotation.Serdeable;

import java.io.Serial;
import java.io.Serializable;

@Serdeable
public class Shopping implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;


    private String id;
    private String idShopping;
    private Order order;
    private double account = 0.0;

    public Shopping(String id, String idShopping, Order order, double account) {
        this.id = id;
        this.idShopping = idShopping;
        this.order = order;
        this.account = account;

    }
    public Shopping(){}

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIdShopping() {
        return idShopping;
    }

    public void setIdShopping(String idShopping) {
        this.idShopping = idShopping;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public double getAccount() {
        return account;
    }

    public void setAccount(double account) {
        this.account = account;
    }

}


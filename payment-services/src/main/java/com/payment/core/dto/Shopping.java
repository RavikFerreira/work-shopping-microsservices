package com.payment.core.dto;

import com.payment.core.dto.enums.State;
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
    private State state;

    public Shopping(String id, String idShopping, Order order, double account, State state) {
        this.id = id;
        this.idShopping = idShopping;
        this.order = order;
        this.account = account;
        this.state = state;

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

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }
}


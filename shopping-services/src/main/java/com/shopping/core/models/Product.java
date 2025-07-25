package com.shopping.core.models;


import io.micronaut.data.annotation.GeneratedValue;
import io.micronaut.data.annotation.Id;
import io.micronaut.data.annotation.MappedEntity;
import io.micronaut.serde.annotation.Serdeable;

import java.io.Serial;
import java.io.Serializable;


@Serdeable
@MappedEntity
public class Product implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue
    private String id;
    private String idProduct;
    private String name;
    private double price = 0.0;
    private int quantity;

    public Product(String id, String idProduct, String name, double price, int quantity) {
        this.id = id;
        this.idProduct = idProduct;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
    }

    public Product(){}

    public void setId(String id) {
        this.id = id;
    }
    public String getId() {
        return id;
    }
    public String getIdProduct() {
        return idProduct;
    }

    public void setIdProduct(String idProduct) {
        this.idProduct = idProduct;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

}

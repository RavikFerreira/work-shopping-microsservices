package com.shopping.config.exceptions;


public class UnableToEditAnOrderFromAShopping extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public UnableToEditAnOrderFromAShopping(String message) {
        super(message);
    }
}

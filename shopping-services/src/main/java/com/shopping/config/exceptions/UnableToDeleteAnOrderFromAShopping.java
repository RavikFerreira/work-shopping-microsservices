package com.shopping.config.exceptions;

public class UnableToDeleteAnOrderFromAShopping extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public UnableToDeleteAnOrderFromAShopping(String message) {
        super(message);
    }
}


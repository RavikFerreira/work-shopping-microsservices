package com.shopping.config.exceptions;

public class CannotDeleteABusyShopping extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public CannotDeleteABusyShopping(String message) {
        super(message);
    }
}


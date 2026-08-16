package com.shopping.config.exceptions.exception;

public class ShoppingResourceNotFoundException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public ShoppingResourceNotFoundException(String message) {
        super(message);
    }
}

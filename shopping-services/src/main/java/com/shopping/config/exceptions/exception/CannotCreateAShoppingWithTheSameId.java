package com.shopping.config.exceptions.exception;

public class CannotCreateAShoppingWithTheSameId extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public CannotCreateAShoppingWithTheSameId(String message) {
        super(message);
    }
}

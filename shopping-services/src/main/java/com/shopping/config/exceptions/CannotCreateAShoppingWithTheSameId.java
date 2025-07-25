package com.shopping.config.exceptions;

public class CannotCreateAShoppingWithTheSameId extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public CannotCreateAShoppingWithTheSameId(String message) {
        super(message);
    }
}

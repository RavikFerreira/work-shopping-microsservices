package com.shopping.config.exceptions;

public class ItIsNotAllowedToAddOrdersWithTheSameId extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public ItIsNotAllowedToAddOrdersWithTheSameId (String message) {
        super(message);
    }
}

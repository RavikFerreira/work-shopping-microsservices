package com.shopping.config.exceptions;

public class ProductResourceNotFoundException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public ProductResourceNotFoundException(String message) {
        super(message);
    }
}

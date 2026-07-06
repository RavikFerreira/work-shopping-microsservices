package com.shopping.config.exceptions;

import java.io.Serial;

public class ShoppingResourceNotFoundException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 1L;

    public ShoppingResourceNotFoundException(String message) {
        super(message);
    }
}

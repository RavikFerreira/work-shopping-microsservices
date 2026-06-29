package com.autorization.config.exceptions.exception;

import java.io.Serial;

public class InvalidEmailOrPasswordExceptions extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 1L;

    public InvalidEmailOrPasswordExceptions(String message) {
        super(message);
    }
}

package com.autorization.config.exceptions;

public class InvalidEmailOrPasswordExceptions extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public InvalidEmailOrPasswordExceptions(String message) {
        super(message);
    }
}

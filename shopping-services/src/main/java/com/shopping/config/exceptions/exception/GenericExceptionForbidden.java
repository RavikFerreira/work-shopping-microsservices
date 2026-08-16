package com.shopping.config.exceptions.exception;

public class GenericExceptionForbidden extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public GenericExceptionForbidden(String message) {
        super(message);
    }
}

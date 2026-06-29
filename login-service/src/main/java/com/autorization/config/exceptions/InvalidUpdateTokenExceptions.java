package com.autorization.config.exceptions;

public class InvalidUpdateTokenExceptions extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public InvalidUpdateTokenExceptions(String message) {
        super(message);
    }
}

package com.autorization.config.exceptions.exception;

import java.io.Serial;
import java.io.Serializable;

public class InvalidUpdateTokenExceptions extends RuntimeException implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    public InvalidUpdateTokenExceptions(String message) {
        super(message);
    }
}

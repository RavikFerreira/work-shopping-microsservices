package com.shopping.config.exceptions;

import java.io.Serial;

public class PaymentNotRealizedException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 1L;

    public PaymentNotRealizedException(String message) {
        super(message);
    }
}
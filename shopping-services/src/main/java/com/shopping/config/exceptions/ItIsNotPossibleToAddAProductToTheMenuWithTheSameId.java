package com.shopping.config.exceptions;


public class ItIsNotPossibleToAddAProductToTheMenuWithTheSameId extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public ItIsNotPossibleToAddAProductToTheMenuWithTheSameId(String message) {
        super(message);
    }
}

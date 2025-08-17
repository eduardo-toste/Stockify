package com.eduardo.stockify.exceptions;

public class ResourceNotFoundException extends RuntimeException {
    public static final String CODE = "NOT_FOUND";

    public ResourceNotFoundException(String message) {
        super(message);
    }

}

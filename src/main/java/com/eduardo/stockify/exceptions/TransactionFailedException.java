package com.eduardo.stockify.exceptions;

public class TransactionFailedException extends RuntimeException {
    public static final String CODE = "CONFLICT";

    public TransactionFailedException(String message) {
        super(message);
    }

}

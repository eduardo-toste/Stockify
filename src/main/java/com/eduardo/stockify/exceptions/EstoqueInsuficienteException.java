package com.eduardo.stockify.exceptions;

public class EstoqueInsuficienteException extends RuntimeException {
    public static final String CODE = "INSUFFICIENT_STOCK";

    public EstoqueInsuficienteException(String message) {
        super(message);
    }
}

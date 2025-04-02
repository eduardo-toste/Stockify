package com.eduardo.stockify.exceptions;

public class EstoqueVazioException extends RuntimeException{

    public EstoqueVazioException(String message) {
        super(message);
    }
}

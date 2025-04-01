package com.eduardo.stockify.exceptions;

public class ProdutoExistenteException extends RuntimeException{

    public ProdutoExistenteException(String message) {
        super(message);
    }
}

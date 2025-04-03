package com.eduardo.stockify.exceptions;

public class UsuarioExistenteException extends RuntimeException {

    public UsuarioExistenteException(String message) {
        super(message);
    }

}

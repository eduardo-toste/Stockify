package com.eduardo.stockify.exceptions;

public class UsuarioExistenteException extends RuntimeException {
    public static final String CODE = "USER_EXISTS";

    public UsuarioExistenteException(String message) {
        super(message);
    }

}

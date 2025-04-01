package com.eduardo.stockify.dtos;

import java.time.LocalDateTime;

public record ErroResponse(
        int status,
        String message,
        Object detalhes,
        LocalDateTime timestamp
) {

    public ErroResponse(int status, String message, Object detalhes) {
        this(status, message, detalhes, LocalDateTime.now());
    }

}

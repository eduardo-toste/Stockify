package com.eduardo.stockify.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record AutenticacaoRequest(
        @NotBlank
        String username,

        @NotBlank
        String password) {
}

package com.eduardo.stockify.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record DadosAutenticacao(
        @NotNull
        @NotBlank
        String username,

        @NotNull
        @NotBlank
        String password) {
}

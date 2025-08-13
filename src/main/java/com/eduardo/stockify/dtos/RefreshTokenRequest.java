package com.eduardo.stockify.dtos;

import jakarta.validation.constraints.NotBlank;

public record RefreshTokenRequest (

        @NotBlank
        String refreshToken

){
}

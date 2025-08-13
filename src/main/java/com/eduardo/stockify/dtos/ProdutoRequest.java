package com.eduardo.stockify.dtos;

import com.eduardo.stockify.models.enums.Categoria;
import jakarta.validation.constraints.*;

public record ProdutoRequest(

        @NotBlank
        @Size(max = 120)
        String nome,

        @Size(max = 2000)
        String descricao,

        @Positive
        double preco,

        @PositiveOrZero
        int quantidade,

        @NotNull
        Categoria categoria) {
}

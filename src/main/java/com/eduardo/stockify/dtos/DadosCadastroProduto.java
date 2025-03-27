package com.eduardo.stockify.dtos;

import com.eduardo.stockify.models.Categoria;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record DadosCadastroProduto(

        @NotNull
        @NotBlank
        String nome,

        String descricao,

        @NotNull
        @Positive
        double preco,

        @NotNull
        @Positive
        int quantidade,

        @NotNull
        Categoria categoria) {
}

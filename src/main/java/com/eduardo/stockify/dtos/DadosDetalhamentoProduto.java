package com.eduardo.stockify.dtos;

import com.eduardo.stockify.models.Categoria;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record DadosDetalhamentoProduto(
        Long id,
        String nome,
        String descricao,
        double preco,
        int quantidade,
        Categoria categoria,
        LocalDateTime dataCadastro) {
}

package com.eduardo.stockify.dtos;

import com.eduardo.stockify.models.enums.Categoria;

import java.time.LocalDateTime;

public record ProdutoUpdateResponse(
        Long id,
        String nome,
        String descricao,
        double preco,
        int quantidade,
        Categoria categoria) {

}

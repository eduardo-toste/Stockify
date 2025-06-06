package com.eduardo.stockify.dtos;

import com.eduardo.stockify.models.enums.Categoria;
import com.eduardo.stockify.models.Produto;

import java.time.LocalDateTime;

public record ProdutoResponse(
        Long id,
        String nome,
        String descricao,
        double preco,
        int quantidade,
        Categoria categoria,
        LocalDateTime dataCadastro) {

    public ProdutoResponse(Produto produto) {
        this(produto.getId(), produto.getNome(), produto.getDescricao(), produto.getPreco(),
                produto.getQuantidade(), produto.getCategoria(), produto.getDataCadastro());
    }

}

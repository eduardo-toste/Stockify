package com.eduardo.stockify.dtos;

import com.eduardo.stockify.models.Categoria;
import com.eduardo.stockify.models.Produto;

import java.time.LocalDateTime;

public record DadosDetalhamentoProduto(
        Long id,
        String nome,
        String descricao,
        double preco,
        int quantidade,
        Categoria categoria,
        LocalDateTime dataCadastro) {

    public DadosDetalhamentoProduto(Produto produto) {
        this(produto.getId(), produto.getNome(), produto.getDescricao(), produto.getPreco(),
                produto.getQuantidade(), produto.getCategoria(), produto.getDataCadastro());
    }

}

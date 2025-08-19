package com.eduardo.stockify.utils;

import com.eduardo.stockify.models.Produto;
import com.eduardo.stockify.models.enums.Categoria;

import java.time.LocalDateTime;

public class ProdutoTestUtils {

    public static Produto criarProduto(Long id, String nome) {
        return new Produto(id, nome, "Descrição", 30.0, 5, Categoria.OUTROS, LocalDateTime.now());
    }

    public static Produto criarProdutoPadrao() {
        return criarProduto(1L, "Produto Teste");
    }

}

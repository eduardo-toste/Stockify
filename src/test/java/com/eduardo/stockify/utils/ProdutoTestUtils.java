package com.eduardo.stockify.utils;

import com.eduardo.stockify.models.Produto;
import com.eduardo.stockify.models.enums.Categoria;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;

public class ProdutoTestUtils {

    public static Produto criarProduto(Long id, String nome) {
        return new Produto(id, nome, "Descrição", 30.0, 5, Categoria.OUTROS, LocalDateTime.now());
    }

    public static Produto criarProdutoPadrao() {
        return criarProduto(1L, "Produto Teste");
    }

    public static List<Produto> criarListaProdutosPadrao() {
        return List.of(
                criarProduto(1L, "Produto A"),
                criarProduto(2L, "Produto B"),
                criarProduto(3L, "Produto C")
        );
    }

    public static Page<Produto> criarPaginaProdutosPadrao(Pageable pageable) {
        List<Produto> lista = criarListaProdutosPadrao();
        return new PageImpl<>(lista, pageable, lista.size());
    }

    public static Page<Produto> criarPaginaPersonalizada(List<Produto> produtos, Pageable pageable) {
        return new PageImpl<>(produtos, pageable, produtos.size());
    }
}
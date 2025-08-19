package com.eduardo.stockify.utils;

import com.eduardo.stockify.models.Movimentacao;
import com.eduardo.stockify.models.Produto;
import com.eduardo.stockify.models.enums.TipoMovimentacao;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;

public class MovimentacaoTestUtils {

    public static Movimentacao criarMovimentacao(Long id, Produto produto, TipoMovimentacao tipo, int quantidade) {
        return new Movimentacao(id, produto, tipo, quantidade, LocalDateTime.now());
    }

    public static Movimentacao criarMovimentacaoPadrao() {
        Produto produto = ProdutoTestUtils.criarProdutoPadrao();
        return criarMovimentacao(1L, produto, TipoMovimentacao.ENTRADA, 1);
    }

    public static List<Movimentacao> criarListaMovimentacoesPadrao() {
        Produto produto = ProdutoTestUtils.criarProdutoPadrao();

        return List.of(
                criarMovimentacao(1L, produto, TipoMovimentacao.ENTRADA, 3),
                criarMovimentacao(2L, produto, TipoMovimentacao.SAIDA, 2),
                criarMovimentacao(3L, produto, TipoMovimentacao.ENTRADA, 1)
        );
    }

    public static List<Movimentacao> criarListaPersonalizada(Produto produto) {
        return List.of(
                criarMovimentacao(10L, produto, TipoMovimentacao.ENTRADA, 5),
                criarMovimentacao(11L, produto, TipoMovimentacao.SAIDA, 2)
        );
    }

    public static Page<Movimentacao> criarPaginaPadrao(Pageable pageable) {
        List<Movimentacao> lista = criarListaMovimentacoesPadrao();
        return new PageImpl<>(lista, pageable, lista.size());
    }
}
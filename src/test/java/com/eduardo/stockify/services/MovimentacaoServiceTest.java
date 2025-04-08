package com.eduardo.stockify.services;

import com.eduardo.stockify.dtos.MovimentacaoRequest;
import com.eduardo.stockify.dtos.MovimentacaoResponse;
import com.eduardo.stockify.exceptions.AlteracaoFalhouException;
import com.eduardo.stockify.exceptions.EstoqueVazioException;
import com.eduardo.stockify.models.Movimentacao;
import com.eduardo.stockify.models.Produto;
import com.eduardo.stockify.models.enums.Categoria;
import com.eduardo.stockify.models.enums.TipoMovimentacao;
import com.eduardo.stockify.repositories.MovimentacaoRepository;
import com.eduardo.stockify.repositories.ProdutoRepository;
import com.eduardo.stockify.services.validations.ValidacaoEspecifica;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

class MovimentacaoServiceTest {

    @InjectMocks
    private MovimentacaoService service;

    @Mock
    private MovimentacaoRepository movimentacaoRepository;

    @Mock
    private ProdutoRepository produtoRepository;

    @Mock
    private ValidacaoEspecifica validacaoEspecificaMock;

    private Produto produto;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        ReflectionTestUtils.setField(service, "validacaoEspecifica", List.of(validacaoEspecificaMock));

        produto = new Produto(
                1L,
                "Teclado Mecânico",
                "Switch Red, RGB, ABNT2",
                300.0,
                10,
                Categoria.ELETRONICO,
                LocalDateTime.now()
        );
    }

    @Test
    void deveRealizarMovimentacaoEntradaComSucesso() {
        var request = new MovimentacaoRequest(1L, TipoMovimentacao.ENTRADA, 5);
        when(produtoRepository.getReferenceById(1L)).thenReturn(produto);
        when(produtoRepository.verificarQuantidade(1L)).thenReturn(10);
        when(produtoRepository.alterarQuantidade(1L, 15)).thenReturn(1);
        when(movimentacaoRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        var response = service.movimentacao(request);

        assertThat(response).isInstanceOf(MovimentacaoResponse.class);
        assertThat(response.tipoMovimentacao()).isEqualTo(TipoMovimentacao.ENTRADA);
        verify(validacaoEspecificaMock).validar(1L);
    }

    @Test
    void deveLancarExcecaoParaSaidaComQuantidadeMaiorQueEstoque() {
        var request = new MovimentacaoRequest(1L, TipoMovimentacao.SAIDA, 20);
        when(produtoRepository.verificarQuantidade(1L)).thenReturn(10);

        assertThatThrownBy(() ->
                service.alterarQuantidade(1L, 20, TipoMovimentacao.SAIDA)
        ).isInstanceOf(RuntimeException.class)
                .hasMessageContaining("maior que a quantidade em estoque");
    }

    @Test
    void deveLancarExcecaoSeNenhumaLinhaForAlterada() {
        var request = new MovimentacaoRequest(1L, TipoMovimentacao.ENTRADA, 5);
        when(produtoRepository.getReferenceById(1L)).thenReturn(produto);
        when(produtoRepository.verificarQuantidade(1L)).thenReturn(10);
        when(produtoRepository.alterarQuantidade(1L, 15)).thenReturn(0); // alteração falhou

        assertThatThrownBy(() -> service.movimentacao(request))
                .isInstanceOf(AlteracaoFalhouException.class);
    }

    @Test
    void deveListarMovimentacoesComSucesso() {
        var pageable = PageRequest.of(0, 10);
        var movimentacao = new Movimentacao(1L, produto, TipoMovimentacao.ENTRADA, 5, LocalDateTime.now());

        when(movimentacaoRepository.findAll(pageable))
                .thenReturn(new PageImpl<>(List.of(movimentacao)));

        var result = service.listarMovimentacoes(pageable);

        assertThat(result.getContent()).hasSize(1);
    }

    @Test
    void deveLancarExcecaoQuandoNaoHouverMovimentacoes() {
        var pageable = PageRequest.of(0, 10);
        when(movimentacaoRepository.findAll(pageable)).thenReturn(Page.empty(pageable));

        assertThatThrownBy(() -> service.listarMovimentacoes(pageable))
                .isInstanceOf(EstoqueVazioException.class)
                .hasMessageContaining("Não existem movimentações!");
    }


    @Test
    void deveListarMovimentacaoPorId() {
        var movimentacao = new Movimentacao(1L, produto, TipoMovimentacao.SAIDA, 3, LocalDateTime.now());
        when(movimentacaoRepository.findById(1L)).thenReturn(Optional.of(movimentacao));

        var response = service.listarMovimentacoesPorId(1L);

        assertThat(response.quantidade()).isEqualTo(3);
    }
}

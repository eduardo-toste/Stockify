package com.eduardo.stockify.services;

import com.eduardo.stockify.dtos.MovimentacaoRequest;
import com.eduardo.stockify.dtos.MovimentacaoResponse;
import com.eduardo.stockify.exceptions.EstoqueInsuficienteException;
import com.eduardo.stockify.exceptions.ResourceNotFoundException;
import com.eduardo.stockify.models.Movimentacao;
import com.eduardo.stockify.models.Produto;
import com.eduardo.stockify.models.enums.Categoria;
import com.eduardo.stockify.models.enums.TipoMovimentacao;
import com.eduardo.stockify.repositories.MovimentacaoRepository;
import com.eduardo.stockify.repositories.ProdutoRepository;
import com.eduardo.stockify.utils.MovimentacaoTestUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MovimentacaoServiceTest {

    @InjectMocks
    private MovimentacaoService movimentacaoService;

    @Mock
    private MovimentacaoRepository movimentacaoRepository;

    @Mock
    private ProdutoRepository produtoRepository;

    @Test
    void deveCriarMovimentacao_QuandoProdutoExiste() {
        // Arrange
        var request = new MovimentacaoRequest(1L, TipoMovimentacao.ENTRADA, 1);
        var produto = new Produto(1L, "Produto Teste", "Testando", 20.0, 2, Categoria.OUTROS, LocalDateTime.now());
        when(produtoRepository.existsById(request.produtoId())).thenReturn(true);
        when(produtoRepository.getReferenceById(request.produtoId())).thenReturn(produto);
        when(produtoRepository.aplicarDeltaDeEstoque(1L, 1)).thenReturn(1);

        // Act
        var result = movimentacaoService.criarMovimentacao(request);

        // Assert
        assertEquals(request.produtoId(), result.produtoId());
        assertEquals(request.tipo(), result.tipoMovimentacao());
        assertEquals(request.quantidade(), result.quantidade());
        assertNotNull(result.data());
        verify(movimentacaoRepository).save(any(Movimentacao.class));
    }

    @Test
    void deveLancarExcecao_QuandoProdutoNaoExiste() {
        // Arrange
        var request = new MovimentacaoRequest(1L, TipoMovimentacao.ENTRADA, 1);
        when(produtoRepository.existsById(request.produtoId())).thenReturn(false);

        // Act
        var exception = assertThrows(ResourceNotFoundException.class,
                () -> movimentacaoService.criarMovimentacao(request));

        // Assert
        assertEquals("Produto não encontrado", exception.getMessage());
    }

    @Test
    void deveAlterarQuantidade_QuandoTiverEstoqueSuficiente() {
        // Arrange
        var produtoId = 1L;
        var quantidadeMovimentada = 1;
        var tipoMovimentacao = TipoMovimentacao.SAIDA;
        var delta = -quantidadeMovimentada;
        when(produtoRepository.aplicarDeltaDeEstoque(produtoId, delta)).thenReturn(1);

        // Act
        movimentacaoService.alterarQuantidade(produtoId, quantidadeMovimentada, tipoMovimentacao);

        // Assert
        verify(produtoRepository).aplicarDeltaDeEstoque(produtoId, delta);
    }

    @Test
    void deveLancarExcecao_QuantoEstoqueForInsuficiente() {
        // Arrange
        var produtoId = 1L;
        var quantidadeMovimentada = 1;
        var tipoMovimentacao = TipoMovimentacao.SAIDA;
        var delta = -quantidadeMovimentada;
        when(produtoRepository.aplicarDeltaDeEstoque(produtoId, delta)).thenReturn(0);
        when(produtoRepository.existsById(produtoId)).thenReturn(true);

        // Act
        var exception = assertThrows(EstoqueInsuficienteException.class,
                () -> movimentacaoService.alterarQuantidade(produtoId, quantidadeMovimentada, tipoMovimentacao));

        // Assert
        assertEquals("Estoque insuficiente para a movimentação solicitada.", exception.getMessage());
    }

    @Test
    void deveLancarExcecao_QuantoProdutoNaoForEncontrado() {
        // Arrange
        var produtoId = 1L;
        var quantidadeMovimentada = 1;
        var tipoMovimentacao = TipoMovimentacao.SAIDA;
        var delta = -quantidadeMovimentada;
        when(produtoRepository.aplicarDeltaDeEstoque(produtoId, delta)).thenReturn(0);
        when(produtoRepository.existsById(produtoId)).thenReturn(false);

        // Act
        var exception = assertThrows(ResourceNotFoundException.class,
                () -> movimentacaoService.alterarQuantidade(produtoId, quantidadeMovimentada, tipoMovimentacao));

        // Assert
        assertEquals("Produto %d não encontrado.".formatted(produtoId), exception.getMessage());
    }

    @Test
    void deveListarMovimentacoesComPaginacao() {
        // Arrange
        Pageable pageable = PageRequest.of(0, 10);
        var pageMovimentacoes = MovimentacaoTestUtils.criarPaginaPadrao(pageable);
        when(movimentacaoRepository.findAll(pageable)).thenReturn(pageMovimentacoes);

        // Act
        var result = movimentacaoService.listarMovimentacoes(pageable);

        // Assert
        assertEquals(3, result.getTotalElements());
        assertEquals(1L, result.getContent().get(0).produtoId());
        assertEquals(TipoMovimentacao.ENTRADA, result.getContent().get(0).tipoMovimentacao());
        verify(movimentacaoRepository).findAll(pageable);
    }

    @Test
    void deveRetornarMovimentacao_QuandoElaExistir() {
        // Arrange
        var produtoId = 1L;
        var movimentacao = MovimentacaoTestUtils.criarMovimentacaoPadrao();
        when(movimentacaoRepository.findById(produtoId)).thenReturn(Optional.of(movimentacao));

        // Act
        var result = movimentacaoService.listarMovimentacoesPorId(produtoId);

        // Assert
        assertEquals(1L, result.id());
        assertEquals(1L, result.produtoId());
        assertEquals(TipoMovimentacao.ENTRADA, result.tipoMovimentacao());
        assertEquals(1, result.quantidade());
    }

    @Test
    void deveLancarExcecao_QuandoMovimentacaoNaoExistir() {
        // Arrange
        var produtoId = 1L;
        when(movimentacaoRepository.findById(produtoId)).thenReturn(Optional.empty());

        // Act
        var exception = assertThrows(ResourceNotFoundException.class,
                () -> movimentacaoService.listarMovimentacoesPorId(produtoId));

        // Assert
        assertEquals("Movimentação não encontrada", exception.getMessage());
    }

}
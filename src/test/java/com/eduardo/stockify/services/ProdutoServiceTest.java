package com.eduardo.stockify.services;

import com.eduardo.stockify.dtos.ProdutoRequest;
import com.eduardo.stockify.exceptions.AlteracaoFalhouException;
import com.eduardo.stockify.exceptions.EstoqueVazioException;
import com.eduardo.stockify.models.Produto;
import com.eduardo.stockify.models.enums.Categoria;
import com.eduardo.stockify.repositories.ProdutoRepository;
import com.eduardo.stockify.services.validations.ValidacaoEspecifica;
import com.eduardo.stockify.services.validations.ValidacaoGeral;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProdutoServiceTest {

    @InjectMocks
    private ProdutoService produtoService;

    @Mock
    private ProdutoRepository repository;

    @Mock
    private List<ValidacaoGeral> validacaoGeral;

    @Mock
    private List<ValidacaoEspecifica> validacaoEspecifica;

    private ProdutoRequest produtoRequest;
    private Produto produto;

    @BeforeEach
    void setUp() {
        produtoRequest = new ProdutoRequest(
                "Teclado Mecânico",
                "Switch Red, RGB, ABNT2",
                259.90,
                10,
                Categoria.ELETRONICO
        );

        produto = new Produto(
                1L,
                produtoRequest.nome(),
                produtoRequest.descricao(),
                produtoRequest.preco(),
                produtoRequest.quantidade(),
                produtoRequest.categoria(),
                LocalDateTime.now()
        );
    }

    @Test
    @DisplayName("Deve criar produto com sucesso")
    void criarProdutoCase1() {
        when(repository.save(any(Produto.class))).thenReturn(produto);

        var response = produtoService.criarProduto(produtoRequest);

        assertThat(response).isNotNull();
        assertThat(response.nome()).isEqualTo(produtoRequest.nome());

        verify(validacaoGeral).forEach(any());
        verify(repository).save(any(Produto.class));
    }

    @Test
    @DisplayName("Deve listar produtos com sucesso")
    void listarProdutosCase1() {
        var page = new PageImpl<>(List.of(produto));
        when(repository.findAll(any(PageRequest.class))).thenReturn(page);

        var result = produtoService.listarProdutos(PageRequest.of(0, 10));

        assertThat(result).hasSize(1);
    }

    @Test
    @DisplayName("Deve lançar exceção quando lista estiver vazia")
    void listarProdutosCase2() {
        when(repository.findAll(any(PageRequest.class))).thenReturn(Page.empty());

        assertThatThrownBy(() -> produtoService.listarProdutos(PageRequest.of(0, 10)))
                .isInstanceOf(EstoqueVazioException.class)
                .hasMessageContaining("Não existem produtos cadastrados!");
    }

    @Test
    @DisplayName("Deve retornar produto por ID")
    void listarProdutoPorIdCase1() {
        when(repository.findById(1L)).thenReturn(Optional.of(produto));

        var response = produtoService.listarProdutoPorId(1L);

        assertThat(response).isNotNull();
        assertThat(response.nome()).isEqualTo(produto.getNome());

        verify(validacaoEspecifica).forEach(any());
    }

    @Test
    @DisplayName("Deve excluir produto por ID")
    void excluirProdutoCase1() {
        produtoService.excluirProduto(1L);

        verify(validacaoEspecifica).forEach(any());
        verify(repository).deleteById(1L);
    }

    @Test
    @DisplayName("Deve alterar produto com sucesso")
    void alterarProdutoCase1() {
        when(repository.atualizarProduto(
                eq(1L),
                anyString(), anyString(), anyDouble(), anyInt(), any(Categoria.class)))
                .thenReturn(1);

        when(repository.findById(1L)).thenReturn(Optional.of(produto));

        var response = produtoService.alterarProduto(1L, produtoRequest);

        assertThat(response).isNotNull();
        assertThat(response.nome()).isEqualTo(produto.getNome());
    }

    @Test
    @DisplayName("Deve lançar exceção quando alteração falhar")
    void alterarProdutoCase2() {
        when(repository.atualizarProduto(
                anyLong(), anyString(), anyString(), anyDouble(), anyInt(), any(Categoria.class)))
                .thenReturn(0);

        assertThatThrownBy(() -> produtoService.alterarProduto(1L, produtoRequest))
                .isInstanceOf(AlteracaoFalhouException.class)
                .hasMessageContaining("A alteração do registro falhou!");
    }

}
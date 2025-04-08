package com.eduardo.stockify.services;

import com.eduardo.stockify.dtos.ProdutoRequest;
import com.eduardo.stockify.dtos.ProdutoResponse;
import com.eduardo.stockify.exceptions.EstoqueVazioException;
import com.eduardo.stockify.models.Produto;
import com.eduardo.stockify.models.enums.Categoria;
import com.eduardo.stockify.repositories.ProdutoRepository;
import com.eduardo.stockify.services.validations.ValidacaoEspecifica;
import com.eduardo.stockify.services.validations.ValidacaoGeral;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProdutoServiceTest {

    @Mock
    private ProdutoRepository repository;

    @Mock
    private ValidacaoGeral validacaoMock;

    @Mock
    private ValidacaoEspecifica validacaoEspecificaMock;

    @InjectMocks
    private ProdutoService produtoService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        ReflectionTestUtils.setField(produtoService, "validacaoGeral", List.of(validacaoMock));
        ReflectionTestUtils.setField(produtoService, "validacaoEspecifica", List.of(validacaoEspecificaMock));
    }


    @Test
    @DisplayName("Deve criar um produto com sucesso")
    void deveCriarProdutoComSucesso() {
        ProdutoRequest request = criarProdutoRequest();
        Produto produtoSalvo = criarProdutoSalvo(request);

        when(repository.save(any(Produto.class))).thenReturn(produtoSalvo);

        ProdutoResponse response = produtoService.criarProduto(request);

        assertNotNull(response);
        assertEquals(request.nome(), response.nome());
        assertEquals(request.descricao(), response.descricao());
        assertEquals(request.preco(), response.preco());
        assertEquals(request.quantidade(), response.quantidade());
        assertEquals(request.categoria(), response.categoria());

        verify(validacaoMock).validar(request);
        verify(repository).save(any(Produto.class));
    }

    @Test
    @DisplayName("Não deve criar produto com dados inválidos")
    void naoDeveCriarProdutoComDadosInvalidos() {
        ProdutoRequest request = criarProdutoRequest();

        doThrow(new IllegalArgumentException("Nome inválido"))
                .when(validacaoMock).validar(request);

        assertThrows(IllegalArgumentException.class, () -> produtoService.criarProduto(request));

        verify(validacaoMock).validar(request);
        verify(repository, never()).save(any(Produto.class));
    }

    @Test
    @DisplayName("Deve lançar exceção se não houver produtos")
    void deveLancarExcecaoSeEstoqueVazio() {
        Pageable pageable = Pageable.unpaged();
        when(repository.findAll(pageable)).thenReturn(Page.empty());

        EstoqueVazioException exception = assertThrows(
                EstoqueVazioException.class,
                () -> produtoService.listarProdutos(pageable)
        );

        assertEquals("Não existem produtos cadastrados!", exception.getMessage());
    }

    @Test
    @DisplayName("Deve listar produtos com sucesso")
    void deveListarProdutosComSucesso() {
        Pageable pageable = Pageable.unpaged();
        Produto produto = criarProdutoSalvo(criarProdutoRequest());
        when(repository.findAll(pageable)).thenReturn(new PageImpl<>(List.of(produto)));

        var result = produtoService.listarProdutos(pageable);

        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(1, result.getTotalElements());
    }

    @Test
    @DisplayName("Deve retornar produto por ID com sucesso")
    void deveListarProdutoPorIdComSucesso() {
        Long id = 1L;
        Produto produto = criarProdutoSalvo(criarProdutoRequest());
        when(repository.findById(id)).thenReturn(Optional.of(produto));

        ProdutoResponse response = produtoService.listarProdutoPorId(id);

        assertNotNull(response);
        assertEquals(produto.getNome(), response.nome());
        verify(validacaoEspecificaMock).validar(id);
    }

    private ProdutoRequest criarProdutoRequest() {
        return new ProdutoRequest(
                "Mouse Gamer",
                "Mouse com sensor óptico de alta precisão",
                159.90,
                10,
                Categoria.ELETRONICO
        );
    }

    private Produto criarProdutoSalvo(ProdutoRequest request) {
        return new Produto(
                1L,
                request.nome(),
                request.descricao(),
                request.preco(),
                request.quantidade(),
                request.categoria(),
                LocalDateTime.now()
        );
    }
}

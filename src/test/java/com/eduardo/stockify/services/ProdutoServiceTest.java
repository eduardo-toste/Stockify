package com.eduardo.stockify.services;

import com.eduardo.stockify.dtos.ProdutoRequest;
import com.eduardo.stockify.exceptions.ProdutoExistenteException;
import com.eduardo.stockify.exceptions.ResourceNotFoundException;
import com.eduardo.stockify.exceptions.TransactionFailedException;
import com.eduardo.stockify.mapper.ProdutoMapper;
import com.eduardo.stockify.models.Produto;
import com.eduardo.stockify.models.enums.Categoria;
import com.eduardo.stockify.models.enums.TipoMovimentacao;
import com.eduardo.stockify.repositories.ProdutoRepository;
import com.eduardo.stockify.utils.ProdutoTestUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProdutoServiceTest {

    @InjectMocks
    private ProdutoService produtoService;

    @Mock
    private ProdutoRepository produtoRepository;

    @Test
    void deveCriarProduto_QuandoEleAindaNaoExistir() {
        // Arrange
        var request = new ProdutoRequest("Produto Teste", "Descrição", 20.0, 5, Categoria.ELETRONICO);
        var produto = ProdutoMapper.toEntity(request);

        when(produtoRepository.existsByNome(produto.getNome())).thenReturn(false);
        when(produtoRepository.save(any(Produto.class))).thenReturn(produto);

        // Act
        var result = produtoService.criarProduto(request);

        // Assert
        assertEquals(request.nome(), result.nome());
        assertEquals(request.descricao(), result.descricao());
        assertEquals(request.preco(), result.preco());
        assertEquals(request.quantidade(), result.quantidade());
        assertEquals(request.quantidade(), result.quantidade());
        verify(produtoRepository).save(any(Produto.class));
    }

    @Test
    void deveLancarExcecao_QuandoProdutoJaExistir() {
        // Arrange
        var request = new ProdutoRequest("Produto Teste", "Descrição", 20.0, 5, Categoria.ELETRONICO);

        when(produtoRepository.existsByNome(request.nome())).thenReturn(true);

        // Act
        var exception = assertThrows(ProdutoExistenteException.class,
                () -> produtoService.criarProduto(request));

        // Assert
        assertEquals("Produto já cadastrado com este nome", exception.getMessage());
    }

    @Test
    void deveListarProdutosComPaginacao() {
        // Arrange
        Pageable pageable = PageRequest.of(0, 10);
        Page<Produto> pageProdutos = ProdutoTestUtils.criarPaginaProdutosPadrao(pageable);
        when(produtoRepository.findAll(pageable)).thenReturn(pageProdutos);

        // Act
        var result = produtoService.listarProdutos(pageable);

        // Assert
        assertEquals(3, result.getTotalElements());
        assertEquals(1L, result.getContent().get(0).id());
        assertEquals(Categoria.OUTROS, result.getContent().get(0).categoria());
        verify(produtoRepository).findAll(pageable);
    }

    @Test
    void deveListarProduto_QuandoEleExistir() {
        // Arrange
        var produto = ProdutoTestUtils.criarProdutoPadrao();
        var produtoResponse = ProdutoMapper.toDTO(produto);
        when(produtoRepository.findById(produto.getId())).thenReturn(Optional.of(produto));

        // Act
        var result = produtoService.listarProdutoPorId(produto.getId());

        // Assert
        assertEquals(produtoResponse.id(), result.id());
        assertEquals(produtoResponse.nome(), result.nome());
        assertEquals(produtoResponse.descricao(), result.descricao());
        assertEquals(produtoResponse.quantidade(), result.quantidade());
        assertEquals(produtoResponse.categoria(), result.categoria());
        assertEquals(produtoResponse.dataCadastro(), result.dataCadastro());
    }

    @Test
    void deveLancarExcecao_QuandoProdutoNaoExistir() {
        // Arrange
        var produto = ProdutoTestUtils.criarProdutoPadrao();
        when(produtoRepository.findById(produto.getId())).thenReturn(Optional.empty());

        // Act
        var exception = assertThrows(ResourceNotFoundException.class,
                () -> produtoService.listarProdutoPorId(produto.getId()));

        // Assert
        assertEquals("Produto não encontrado", exception.getMessage());
    }

    @Test
    void deveExcluirProduto_QuandoEleExistir() {
        // Arrange
        var produto = ProdutoTestUtils.criarProdutoPadrao();
        when(produtoRepository.findById(produto.getId())).thenReturn(Optional.of(produto));

        // Act
        produtoService.excluirProduto(produto.getId());

        // Assert
        verify(produtoRepository).deleteById(produto.getId());
    }

    @Test
    void naoDeveExcluirProduto_QuandoProdutoNaoExistir() {
        // Arrange
        var id = 1L;
        when(produtoRepository.findById(id)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> produtoService.excluirProduto(id));
        verify(produtoRepository, never()).deleteById(any());
    }

    @Test
    void deveAlterarProduto_QuandoEleExistir() {
        // Arrange
        var id = 1L;
        var produto = ProdutoTestUtils.criarProdutoPadrao();
        var request = new ProdutoRequest("Produto Teste", "Descricao", 20.0, 2, Categoria.OUTROS);
        var produtoAtualizado = ProdutoMapper.toEntity(request);
        var response = ProdutoMapper.toDTO(produtoAtualizado);
        when(produtoRepository.findById(id)).thenReturn(Optional.of(produto));
        when(produtoRepository.atualizarProduto(any(Produto.class))).thenReturn(1);

        // Act
        var result = produtoService.alterarProduto(id, request);

        // Assert
        assertEquals(response.nome(), result.nome());
        assertEquals(response.descricao(), result.descricao());
        assertEquals(response.quantidade(), result.quantidade());
        assertEquals(response.categoria(), result.categoria());
    }

    @Test
    void naoDeveAlterarProduto_QuandoNaoEleExistir() {
        // Arrange
        var id = 1L;
        when(produtoRepository.findById(id)).thenReturn(Optional.empty());

        // Act
        var exception = assertThrows(ResourceNotFoundException.class,
                () -> produtoService.listarProdutoPorId(id));

        // Assert
        assertEquals("Produto não encontrado", exception.getMessage());
    }

    @Test
    void naoDeveAlterarProduto_QuandoFalhar() {
        // Arrange
        var id = 1L;
        var produto = ProdutoTestUtils.criarProdutoPadrao();
        var request = new ProdutoRequest("Produto Teste", "Descricao", 20.0, 2, Categoria.OUTROS);
        when(produtoRepository.findById(id)).thenReturn(Optional.of(produto));
        when(produtoRepository.atualizarProduto(any(Produto.class))).thenReturn(0);

        // Act
        var exception = assertThrows(TransactionFailedException.class,
                () -> produtoService.alterarProduto(id, request));

        // Assert
        assertEquals("A alteração falhou", exception.getMessage());
    }

}
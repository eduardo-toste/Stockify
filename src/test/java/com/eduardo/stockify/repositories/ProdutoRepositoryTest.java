package com.eduardo.stockify.repositories;

import com.eduardo.stockify.models.Produto;
import com.eduardo.stockify.models.enums.Categoria;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
class ProdutoRepositoryTest {

    @Autowired
    private ProdutoRepository repository;

    @Autowired
    private EntityManager entityManager;

    private Produto novoProduto(String nome, int quantidade) {
        return new Produto(null, nome, "Descrição", 100.0, quantidade, Categoria.OUTROS, LocalDateTime.now());
    }

    @Test
    void deveVerificarSeProdutoExistePorNome() {
        // Arrange
        repository.save(novoProduto("Teclado", 5));

        // Act
        boolean existe = repository.existsByNome("Teclado");

        // Assert
        assertTrue(existe);
    }

    @Test
    void deveAtualizarProdutoComSucesso() {
        // Arrange
        Produto produto = repository.save(novoProduto("Mouse", 10));
        produto.setNome("Mouse Gamer");
        produto.setPreco(150.0);

        // Act
        int linhasAfetadas = repository.atualizarProduto(produto);

        // Assert
        assertEquals(1, linhasAfetadas);

        Produto atualizado = repository.findById(produto.getId()).orElseThrow();
        assertEquals("Mouse Gamer", atualizado.getNome());
        assertEquals(150.0, atualizado.getPreco());
    }

    @Test
    void deveVerificarQuantidadeDoProduto() {
        // Arrange
        Produto produto = repository.save(novoProduto("Monitor", 7));

        // Act
        int quantidade = repository.verificarQuantidade(produto.getId());

        // Assert
        assertEquals(7, quantidade);
    }

    @Test
    void deveAtualizarQuantidadeDoProduto() {
        // Arrange
        Produto produto = repository.save(novoProduto("Monitor", 7));

        // Act
        int linhasAlteradas = repository.alterarQuantidade(produto.getId(), 10);

        entityManager.flush();
        entityManager.clear();

        var produtoAtualizado = repository.findById(produto.getId()).get();

        // Assert
        assertEquals(1, linhasAlteradas);
        assertEquals(10, produtoAtualizado.getQuantidade());
    }

    @Test
    void deveAplicarDeltaDeEstoque() {
        // Arrange
        Produto produto = repository.save(novoProduto("Monitor", 7));

        // Act
        var linhasAlteradas = repository.aplicarDeltaDeEstoque(produto.getId(), 3);

        entityManager.flush();
        entityManager.clear();

        var produtoAtualizado = repository.findById(produto.getId()).get();

        // Assert
        assertEquals(1, linhasAlteradas);
        assertEquals(10, produtoAtualizado.getQuantidade());
    }

}
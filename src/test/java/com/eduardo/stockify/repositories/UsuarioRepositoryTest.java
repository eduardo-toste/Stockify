package com.eduardo.stockify.repositories;

import com.eduardo.stockify.models.Produto;
import com.eduardo.stockify.models.Usuario;
import com.eduardo.stockify.models.enums.Categoria;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
class UsuarioRepositoryTest {

    @Autowired
    private UsuarioRepository repository;

    @Autowired
    private EntityManager entityManager;

    private Usuario novoUsuario(String username, String password) {
        return new Usuario(null, username, password);
    }

    @Test
    void deveBuscarUsuarioPorUsername_QuandoExistir() {
        // Arrange
        Usuario usuario = repository.save(novoUsuario("teste", "encryptPassword"));

        // Act
        var usuarioEncontrado = repository.findByUsername(usuario.getUsername());

        // Assert
        assertTrue(usuarioEncontrado.isPresent());
    }

    @Test
    void deveBuscarUsuarioPorUsername_QuandoNaoExistir() {
        // Act
        var usuarioEncontrado = repository.findByUsername("teste");

        // Assert
        assertTrue(usuarioEncontrado.isEmpty());;
    }

    @Test
    void deveVerificarExistenciaDoUsuario_QuandoExistir() {
        // Arrange
        Usuario usuario = repository.save(novoUsuario("teste", "encryptPassword"));

        // Act
        var exists = repository.existsByUsername(usuario.getUsername());

        // Assert
        assertTrue(exists);
    }

    @Test
    void deveVerificarExistenciaDoUsuario_QuandoNaoExistir() {
        // Act
        var exists = repository.existsByUsername("teste");

        // Assert
        assertFalse(exists);
    }

}
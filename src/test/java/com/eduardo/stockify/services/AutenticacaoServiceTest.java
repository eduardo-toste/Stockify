package com.eduardo.stockify.services;

import com.eduardo.stockify.dtos.AutenticacaoRequest;
import com.eduardo.stockify.exceptions.UsuarioExistenteException;
import com.eduardo.stockify.models.Usuario;
import com.eduardo.stockify.repositories.UsuarioRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AutenticacaoServiceTest {

    @InjectMocks
    private AutenticacaoService autenticacaoService;

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Test
    void deveRetornarUsuario_QuandoUsernameExiste() {
        // Arrange
        var user = new Usuario(1L, "admin", "password");
        when(usuarioRepository.findByUsername("admin")).thenReturn(Optional.of(user));

        // Act
        UserDetails result = autenticacaoService.loadUserByUsername("admin");

        // Assert
        assertEquals("admin", result.getUsername());
        assertEquals("password", result.getPassword());
    }

    @Test
    void deveLancarExcecao_QuandoUsernameNaoExiste() {
        // Arrange
        when(usuarioRepository.findByUsername("unknown")).thenReturn(Optional.empty());

        // Act
        var exception = assertThrows(UsernameNotFoundException.class,
                () -> autenticacaoService.loadUserByUsername("unknown"));

        // Assert
        assertEquals("Credenciais inválidas", exception.getMessage());
    }

    @Test
    void deveRegistrarUsuario_QuandoDadosValidos() {
        // Arrange
        var request = new AutenticacaoRequest("admin", "password");
        var user = new Usuario(null, "admin", "cryptoPassword");
        when(usuarioRepository.existsByUsername(request.username())).thenReturn(false);
        when(passwordEncoder.encode(request.password())).thenReturn("cryptoPassword");

        // Act
        autenticacaoService.registrar(request);

        // Assert
        verify(usuarioRepository).save(user);
    }

    @Test
    void deveLancarExcecao_QuandoUsuarioJaExistir() {
        // Arrange
        var request = new AutenticacaoRequest("admin", "password");
        when(usuarioRepository.existsByUsername("admin")).thenReturn(true);

        // Act
        var exception = assertThrows(UsuarioExistenteException.class,
                () -> autenticacaoService.registrar(request));

        // Assert
        assertEquals("Usuário já cadastrado", exception.getMessage());
    }

    @Test
    void deveLancarExcecao_QuandoSalvarUsuarioLancarDataIntegrityViolationException() {
        // Arrange
        var request = new AutenticacaoRequest("admin", "password");
        var user = new Usuario(null, "admin", "cryptoPassword");
        when(usuarioRepository.existsByUsername("admin")).thenReturn(false);
        when(passwordEncoder.encode("password")).thenReturn("cryptoPassword");
        // Simula erro ao salvar no banco
        when(usuarioRepository.save(user)).thenThrow(new DataIntegrityViolationException("Erro de integridade"));

        // Act
        var exception = assertThrows(UsuarioExistenteException.class,
                () -> autenticacaoService.registrar(request));

        // Assert
        assertEquals("Usuário já cadastrado", exception.getMessage());
    }

}
package com.eduardo.stockify.controllers;

import com.eduardo.stockify.config.TestSecurityConfig;
import com.eduardo.stockify.models.Usuario;
import com.eduardo.stockify.repositories.UsuarioRepository;
import com.eduardo.stockify.services.MovimentacaoService;
import com.eduardo.stockify.services.TokenService;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@WebMvcTest(MovimentacaoController.class)
@AutoConfigureMockMvc
@Import(TestSecurityConfig.class)
class MovimentacaoControllerTest {

    @Autowired private MockMvc mockMvc;

    @MockBean private MovimentacaoService movimentacaoService;
    @MockBean private TokenService tokenService;
    @MockBean private UsuarioRepository usuarioRepository;

    private final String token = "fake.jwt.token";
    private final String username = "usuario_teste";

    @BeforeEach
    void setup() {
        autenticarUsuarioMock();
    }

    private void autenticarUsuarioMock() {
        when(tokenService.getSubject(eq(token), eq("access"))).thenReturn(username);
        when(usuarioRepository.findByUsername(username))
                .thenReturn(Optional.of(new Usuario(1L, username, "senha")));
    }

}
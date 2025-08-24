package com.eduardo.stockify.controllers;

import com.eduardo.stockify.config.TestSecurityConfig;
import com.eduardo.stockify.dtos.EstatisticasResponse;
import com.eduardo.stockify.models.Usuario;
import com.eduardo.stockify.repositories.UsuarioRepository;
import com.eduardo.stockify.services.EstatisticasService;
import com.eduardo.stockify.services.TokenService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(EstatisticasController.class)
@AutoConfigureMockMvc
@Import(TestSecurityConfig.class)
class EstatisticasControllerTest {

    @Autowired private MockMvc mockMvc;

    @MockBean private EstatisticasService estatisticasService;
    @MockBean private TokenService tokenService;
    @MockBean private UsuarioRepository usuarioRepository;

    private final String token = "fake.jwt.token";
    private final String username = "usuario_teste";

    @BeforeEach
    void setup() {
        autenticarUsuarioMock();
    }

    @Test
    void deveRetornarAsEstatisticasDoEstoquePreenchido() throws Exception {
        // Arrange
        var statsResponse = new EstatisticasResponse(1L, 3L, 30.0, 10.0, 10.0, 10.0);
        when(estatisticasService.estatisticas()).thenReturn(statsResponse);

        // Act
        mockMvc.perform(get("/estatisticas")
                        .header("Authorization", "Bearer " + token))
                // Assert
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.produtos").value(1))
                .andExpect(jsonPath("$.qtdUnidades").value(3))
                .andExpect(jsonPath("$.valorTotal").value(30.0))
                .andExpect(jsonPath("$.mediaPrecos").value(10.0))
                .andExpect(jsonPath("$.precoMinimo").value(10.0))
                .andExpect(jsonPath("$.precoMaximo").value(10.0));
    }

    @Test
    void deveRetornarAsEstatisticasZeradasDoEstoqueVazio() throws Exception {
        // Arrange
        var statsResponse = new EstatisticasResponse(0L, 0L, 0, 0, 0, 0);
        when(estatisticasService.estatisticas()).thenReturn(statsResponse);

        // Act
        mockMvc.perform(get("/estatisticas")
                        .header("Authorization", "Bearer " + token))
                // Assert
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.produtos").value(0))
                .andExpect(jsonPath("$.qtdUnidades").value(0))
                .andExpect(jsonPath("$.valorTotal").value(0))
                .andExpect(jsonPath("$.mediaPrecos").value(0))
                .andExpect(jsonPath("$.precoMinimo").value(0))
                .andExpect(jsonPath("$.precoMaximo").value(0));
    }

    private void autenticarUsuarioMock() {
        when(tokenService.getSubject(eq(token), eq("access"))).thenReturn(username);
        when(usuarioRepository.findByUsername(username))
                .thenReturn(Optional.of(new Usuario(1L, username, "senha")));
    }

}
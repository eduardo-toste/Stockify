package com.eduardo.stockify.controllers;

import com.eduardo.stockify.config.TestSecurityConfig;
import com.eduardo.stockify.dtos.MovimentacaoRequest;
import com.eduardo.stockify.dtos.MovimentacaoResponse;
import com.eduardo.stockify.exceptions.ResourceNotFoundException;
import com.eduardo.stockify.models.Usuario;
import com.eduardo.stockify.models.enums.TipoMovimentacao;
import com.eduardo.stockify.repositories.UsuarioRepository;
import com.eduardo.stockify.services.MovimentacaoService;
import com.eduardo.stockify.services.MovimentacoesExportService;
import com.eduardo.stockify.services.TokenService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageImpl;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(MovimentacaoController.class)
@AutoConfigureMockMvc
@Import(TestSecurityConfig.class)
class MovimentacaoControllerTest {

    @Autowired private MockMvc mockMvc;

    @MockBean private MovimentacaoService movimentacaoService;
    @MockBean private TokenService tokenService;
    @MockBean private UsuarioRepository usuarioRepository;
    @MockBean private MovimentacoesExportService exportService;
    @Autowired private ObjectMapper objectMapper;

    private final String token = "fake.jwt.token";
    private final String username = "usuario_teste";

    @BeforeEach
    void setup() {
        autenticarUsuarioMock();
    }

    @Test
    void deveCriarMovimentacaoComSucesso() throws Exception {
        // Arrange
        var request = new MovimentacaoRequest(1L, TipoMovimentacao.ENTRADA, 2);
        var json = objectMapper.writeValueAsString(request);
        var response = new MovimentacaoResponse(1L, 1L, TipoMovimentacao.ENTRADA, 2, LocalDateTime.now());
        when(movimentacaoService.criarMovimentacao(request)).thenReturn(response);

        // Act
        mockMvc.perform(post("/movimentacao")
                        .header("Authorization", "Bearer " + token)
                        .contentType(APPLICATION_JSON)
                        .content(json))
                // Assert
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.produtoId").value(1))
                .andExpect(jsonPath("$.tipoMovimentacao").value("ENTRADA"))
                .andExpect(jsonPath("$.quantidade").value(2))
                .andExpect(jsonPath("$.data").exists());
    }

    @Test
    void deveRetornarErroValidacao_ProdutoIdNull() throws Exception {
        var request = new MovimentacaoRequest(null, TipoMovimentacao.ENTRADA, 2);

        mockMvc.perform(post("/movimentacao")
                        .header("Authorization", "Bearer " + token)
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.code").value("VALIDATION_ERROR"))
                .andExpect(jsonPath("$.violations[0].field").value("produtoId"));
    }

    @Test
    void deveRetornarErroValidacao_QuantidadeNegativa() throws Exception {
        var request = new MovimentacaoRequest(1L, TipoMovimentacao.ENTRADA, -1);

        mockMvc.perform(post("/movimentacao")
                        .header("Authorization", "Bearer " + token)
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.code").value("VALIDATION_ERROR"))
                .andExpect(jsonPath("$.violations[0].field").value("quantidade"));
    }

    @Test
    void deveListarMovimentacoesComSucesso() throws Exception {
        var page = new PageImpl<>(List.of(
                new MovimentacaoResponse(1L, 1L, TipoMovimentacao.ENTRADA, 10, LocalDateTime.now())
        ));

        when(movimentacaoService.listarMovimentacoes(any())).thenReturn(page);

        mockMvc.perform(get("/movimentacao")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(1));
    }

    @Test
    void deveListarMovimentacaoPorIdComSucesso() throws Exception {
        var response = new MovimentacaoResponse(1L, 1L, TipoMovimentacao.ENTRADA, 10, LocalDateTime.now());

        when(movimentacaoService.listarMovimentacoesPorId(1L)).thenReturn(response);

        mockMvc.perform(get("/movimentacao/1")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void deveRetornarNotFound_QuandoMovimentacaoNaoExiste() throws Exception {
        when(movimentacaoService.listarMovimentacoesPorId(99L))
                .thenThrow(new ResourceNotFoundException("Movimentação não encontrada"));

        mockMvc.perform(get("/movimentacao/99")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value("NOT_FOUND"))
                .andExpect(jsonPath("$.detail").value("Movimentação não encontrada"));
    }

    @Test
    void deveExportarMovimentacoesComSucesso() throws Exception {
        when(exportService.enviarParaS3("Movimentacoes")).thenReturn(Map.of(
                "message", "Arquivo enviado com sucesso",
                "url", "https://bucket/arquivo.csv"
        ));

        mockMvc.perform(get("/movimentacao/exportar")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.url").exists())
                .andExpect(jsonPath("$.message").value("Arquivo enviado com sucesso"));
    }

    @Test
    void deveRetornarErroInterno_QuandoExportacaoFalhar() throws Exception {
        when(exportService.enviarParaS3("Movimentacoes")).thenThrow(new RuntimeException("Ocorreu um erro inesperado. Tente novamente mais tarde."));

        mockMvc.perform(get("/movimentacao/exportar")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.detail").value("Ocorreu um erro inesperado. Tente novamente mais tarde."));
    }

    @Test
    void deveRetornarUnauthorized_QuandoTokenAusente() throws Exception {
        mockMvc.perform(get("/movimentacao"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").value("Token ausente ou inválido"));
    }

    private void autenticarUsuarioMock() {
        when(tokenService.getSubject(eq(token), eq("access"))).thenReturn(username);
        when(usuarioRepository.findByUsername(username))
                .thenReturn(Optional.of(new Usuario(1L, username, "senha")));
    }

}
package com.eduardo.stockify.controllers;

import com.eduardo.stockify.config.TestSecurityConfig;
import com.eduardo.stockify.dtos.MovimentacaoRequest;
import com.eduardo.stockify.dtos.ProdutoRequest;
import com.eduardo.stockify.dtos.ProdutoResponse;
import com.eduardo.stockify.dtos.ProdutoUpdateResponse;
import com.eduardo.stockify.exceptions.ResourceNotFoundException;
import com.eduardo.stockify.models.Usuario;
import com.eduardo.stockify.models.enums.Categoria;
import com.eduardo.stockify.models.enums.TipoMovimentacao;
import com.eduardo.stockify.repositories.ProdutoRepository;
import com.eduardo.stockify.repositories.UsuarioRepository;
import com.eduardo.stockify.services.ProdutoExportService;
import com.eduardo.stockify.services.ProdutoService;
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
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ProdutoController.class)
@AutoConfigureMockMvc
@Import(TestSecurityConfig.class)
class ProdutoControllerTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;

    @MockBean private ProdutoService produtoService;
    @MockBean private ProdutoExportService exportService;
    @MockBean private UsuarioRepository usuarioRepository;
    @MockBean private TokenService tokenService;

    private final String token = "fake.jwt.token";
    private final String username = "usuario_teste";

    @BeforeEach
    void setup() {
        autenticarUsuarioMock();
    }

    @Test
    void deveCriarOProdutoComSucesso() throws Exception {
        // Arrange
        var request = new ProdutoRequest("Produto Teste", "Descricao", 20.0, 2, Categoria.OUTROS);
        var response = new ProdutoResponse(1L, "Produto Teste", "Descricao", 20.0, 2, Categoria.OUTROS, LocalDateTime.now());
        when(produtoService.criarProduto(request)).thenReturn(response);

        // Act
        mockMvc.perform(post("/produtos")
                        .header("Authorization", "Bearer " + token)
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                // Assert
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nome").value("Produto Teste"))
                .andExpect(jsonPath("$.descricao").value("Descricao"))
                .andExpect(jsonPath("$.preco").value(20.0))
                .andExpect(jsonPath("$.quantidade").value(2))
                .andExpect(jsonPath("$.categoria").value("OUTROS"));
    }

    @Test
    void deveRetornarErroValidacao_ProdutoIdNull() throws Exception {
        var request = new ProdutoRequest(null, "Descricao", 20.0, 2, Categoria.OUTROS);

        mockMvc.perform(post("/produtos")
                        .header("Authorization", "Bearer " + token)
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.code").value("VALIDATION_ERROR"))
                .andExpect(jsonPath("$.violations[0].field").value("nome"));
    }

    @Test
    void deveRetornarErroValidacao_QuantidadeNegativa() throws Exception {
        var request = new ProdutoRequest("Produto Teste", "Descricao", 20.0, -1, Categoria.OUTROS);

        mockMvc.perform(post("/produtos")
                        .header("Authorization", "Bearer " + token)
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.code").value("VALIDATION_ERROR"))
                .andExpect(jsonPath("$.violations[0].field").value("quantidade"));
    }

    @Test
    void deveListarProdutosComSucesso() throws Exception {
        // Arrange
        var page = new PageImpl<>(List.of(
                new ProdutoResponse(1L, "Produto Teste", "Descricao", 20.0, 2, Categoria.OUTROS, LocalDateTime.now())
        ));

        when(produtoService.listarProdutos(any())).thenReturn(page);

        // Act
        mockMvc.perform(get("/produtos")
                        .header("Authorization", "Bearer " + token))
                // Assert
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(1));
    }

    @Test
    void deveListarProdutoPeloIdComSucesso() throws Exception {
        // Arrange
        var id = 1L;
        var response = new ProdutoResponse(1L, "Produto Teste", "Descricao", 20.0, 2, Categoria.OUTROS, LocalDateTime.now());

        when(produtoService.listarProdutoPorId(id)).thenReturn(response);

        // Act
        mockMvc.perform(get("/produtos/1")
                        .header("Authorization", "Bearer " + token))
                // Assert
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nome").value("Produto Teste"))
                .andExpect(jsonPath("$.descricao").value("Descricao"))
                .andExpect(jsonPath("$.preco").value(20.0))
                .andExpect(jsonPath("$.quantidade").value(2))
                .andExpect(jsonPath("$.categoria").value("OUTROS"));
    }

    @Test
    void deveRetornarNotFound_QuandoProdutoNaoExiste() throws Exception {
        // Arrange
        when(produtoService.listarProdutoPorId(99L))
                .thenThrow(new ResourceNotFoundException("Produto não encontrado"));

        // Act
        mockMvc.perform(get("/produtos/99")
                        .header("Authorization", "Bearer " + token))
                // Assert
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value("NOT_FOUND"))
                .andExpect(jsonPath("$.detail").value("Produto não encontrado"));
    }

    @Test
    void deveExportarProdutosComSucesso() throws Exception {
        when(exportService.enviarParaS3("Produtos")).thenReturn(Map.of(
                "message", "Arquivo enviado com sucesso",
                "url", "https://bucket/arquivo.csv"
        ));

        mockMvc.perform(get("/produtos/exportar")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.url").exists())
                .andExpect(jsonPath("$.message").value("Arquivo enviado com sucesso"));
    }

    @Test
    void deveRetornarErroInterno_QuandoExportacaoFalhar() throws Exception {
        when(exportService.enviarParaS3("Produtos")).thenThrow(new RuntimeException("Ocorreu um erro inesperado. Tente novamente mais tarde."));

        mockMvc.perform(get("/produtos/exportar")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.detail").value("Ocorreu um erro inesperado. Tente novamente mais tarde."));
    }

    @Test
    void deveRetornarUnauthorized_QuandoTokenAusente() throws Exception {
        mockMvc.perform(get("/produtos"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").value("Token ausente ou inválido"));
    }

    @Test
    void deveAtualizarProdutoComSucesso() throws Exception {
        var id = 1L;
        var request = new ProdutoRequest("Produto Atualizado", "Nova Descricao", 30.0, 5, Categoria.ELETRONICO);
        var response = new ProdutoUpdateResponse(id, "Produto Atualizado", "Nova Descricao", 30.0, 5, Categoria.ELETRONICO);

        when(produtoService.alterarProduto(eq(id), any())).thenReturn(response);

        mockMvc.perform(put("/produtos/{id}", id)
                        .header("Authorization", "Bearer " + token)
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("Produto Atualizado"))
                .andExpect(jsonPath("$.descricao").value("Nova Descricao"))
                .andExpect(jsonPath("$.preco").value(30.0))
                .andExpect(jsonPath("$.quantidade").value(5))
                .andExpect(jsonPath("$.categoria").value("ELETRONICO"));
    }

    @Test
    void deveRetornarErroValidacao_AoAtualizarProduto() throws Exception {
        var request = new ProdutoRequest("", "Descricao", -10.0, -5, Categoria.OUTROS);

        mockMvc.perform(put("/produtos/1")
                        .header("Authorization", "Bearer " + token)
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.code").value("VALIDATION_ERROR"));
    }

    @Test
    void deveRetornarNotFound_AoAtualizarProdutoInexistente() throws Exception {
        var request = new ProdutoRequest("Produto X", "Desc", 10.0, 1, Categoria.OUTROS);

        when(produtoService.alterarProduto(eq(999L), any()))
                .thenThrow(new ResourceNotFoundException("Produto não encontrado"));

        mockMvc.perform(put("/produtos/999")
                        .header("Authorization", "Bearer " + token)
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value("NOT_FOUND"))
                .andExpect(jsonPath("$.detail").value("Produto não encontrado"));
    }

    @Test
    void deveDeletarProdutoComSucesso() throws Exception {
        mockMvc.perform(delete("/produtos/1")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isNoContent());
    }

    @Test
    void deveRetornarNotFound_AoDeletarProdutoInexistente() throws Exception {
        // Arrange
        doThrow(new ResourceNotFoundException("Produto não encontrado"))
                .when(produtoService).excluirProduto(999L);

        // Act & Assert
        mockMvc.perform(delete("/produtos/999")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value("NOT_FOUND"))
                .andExpect(jsonPath("$.detail").value("Produto não encontrado"));
    }

    private void autenticarUsuarioMock() {
        when(tokenService.getSubject(eq(token), eq("access"))).thenReturn(username);
        when(usuarioRepository.findByUsername(username))
                .thenReturn(Optional.of(new Usuario(1L, username, "senha")));
    }

}
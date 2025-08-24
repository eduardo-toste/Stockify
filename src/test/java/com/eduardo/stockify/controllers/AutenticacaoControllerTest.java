package com.eduardo.stockify.controllers;

import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.eduardo.stockify.config.TestSecurityConfig;
import com.eduardo.stockify.dtos.AutenticacaoRequest;
import com.eduardo.stockify.dtos.RefreshTokenRequest;
import com.eduardo.stockify.repositories.UsuarioRepository;
import com.eduardo.stockify.services.AutenticacaoService;
import com.eduardo.stockify.services.TokenService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.User;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Date;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import com.auth0.jwt.JWT;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SuppressWarnings("removal")
@WebMvcTest(AutenticacaoController.class)
@AutoConfigureMockMvc
@Import(TestSecurityConfig.class)
class AutenticacaoControllerTest {

    @Autowired private MockMvc mockMvc;
    @Autowired  private ObjectMapper objectMapper;

    @MockBean private AuthenticationManager authenticationManager;
    @MockBean private TokenService tokenService;
    @MockBean private AutenticacaoService autenticacaoService;
    @MockBean private UsuarioRepository usuarioRepository;

    @Test
    void deveAutenticarComSucessoERetornarTokens() throws Exception {
        // Arrange
        var request = new AutenticacaoRequest("username", "password");
        var requestJson = objectMapper.writeValueAsString(request);

        var userDetails = User
                .withUsername("username")
                .password("password")
                .build();

        var auth = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

        when(authenticationManager.authenticate(any())).thenReturn(auth);
        when(tokenService.gerarAccessToken("username")).thenReturn("access-token");
        when(tokenService.gerarRefreshToken("username")).thenReturn("refresh-token");

        // Act
        mockMvc.perform(post("/auth/login")
                        .contentType("application/json")
                        .content(requestJson))
                // Assert
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").value("access-token"))
                .andExpect(jsonPath("$.refreshToken").value("refresh-token"));
    }

    @Test
    void deveRetornar401ComCorpoProblemJson_QuandoCredenciaisInvalidas() throws Exception {
        // Arrange
        var request = new AutenticacaoRequest("user", "wrong-pass");
        var json = objectMapper.writeValueAsString(request);

        when(authenticationManager.authenticate(any()))
                .thenThrow(new BadCredentialsException("Credenciais inválidas"));

        // Act + Assert
        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.type").value("https://stockify/errors/auth_failed"))
                .andExpect(jsonPath("$.title").value("Não autenticado"))
                .andExpect(jsonPath("$.status").value(401))
                .andExpect(jsonPath("$.detail").value("Credenciais inválidas"))
                .andExpect(jsonPath("$.instance").value("/auth/login"))
                .andExpect(jsonPath("$.code").value("AUTH_FAILED"))
                .andExpect(jsonPath("$.timestamp").exists());
    }

    @Test
    void deveGerarNovosTokenComRefreshValido() throws Exception {
        // Arrange
        var request = new RefreshTokenRequest("validRefresh");
        var json = objectMapper.writeValueAsString(request);

        when(tokenService.getSubject("validRefresh", "refresh")).thenReturn("username123");
        when(tokenService.gerarAccessToken("username123")).thenReturn("accessToken");
        when(tokenService.gerarRefreshToken("username123")).thenReturn("refreshToken");

        // Act
        mockMvc.perform(post("/auth/refresh")
                        .contentType("application/json")
                        .content(json))
                // Assert
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").value("accessToken"))
                .andExpect(jsonPath("$.refreshToken").value("refreshToken"));
    }

    @Test
    void deveRetornar401_QuandoRefreshTokenAusente() throws Exception {
        // Arrange
        var request = new RefreshTokenRequest(null);
        var json = objectMapper.writeValueAsString(request);

        // Act & Assert
        mockMvc.perform(post("/auth/refresh")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.type").value("https://stockify/errors/auth_failed"))
                .andExpect(jsonPath("$.title").value("Não autenticado"))
                .andExpect(jsonPath("$.status").value(401))
                .andExpect(jsonPath("$.detail").value("Refresh token ausente"))
                .andExpect(jsonPath("$.code").value("AUTH_FAILED"))
                .andExpect(jsonPath("$.instance").value("/auth/refresh"))
                .andExpect(jsonPath("$.timestamp").exists());
    }

    @Test
    void deveRetornar401_QuandoRefreshTokenInvalido() throws Exception {
        // Arrange
        var request = new RefreshTokenRequest("invalidToken");
        var json = objectMapper.writeValueAsString(request);

        when(tokenService.getSubject("invalidToken", "refresh"))
                .thenThrow(new JWTVerificationException("Token inválido ou expirado"));

        // Act & Assert
        mockMvc.perform(post("/auth/refresh")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.type").value("https://stockify/errors/auth_failed"))
                .andExpect(jsonPath("$.title").value("Não autenticado"))
                .andExpect(jsonPath("$.status").value(401))
                .andExpect(jsonPath("$.detail").value("Token inválido ou expirado"))
                .andExpect(jsonPath("$.code").value("AUTH_FAILED"))
                .andExpect(jsonPath("$.instance").value("/auth/refresh"))
                .andExpect(jsonPath("$.timestamp").exists());
    }

    @Test
    void deveRetornar401_QuandoTipoDoTokenForInvalido() throws Exception {
        // Arrange
        String tokenComTipoErrado = gerarTokenComTipo("access"); // helper abaixo
        var request = new RefreshTokenRequest(tokenComTipoErrado);
        var json = objectMapper.writeValueAsString(request);

        when(tokenService.getSubject(tokenComTipoErrado, "refresh"))
                .thenThrow(new JWTVerificationException("Tipo de token inválido!"));

        // Act & Assert
        mockMvc.perform(post("/auth/refresh")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.detail").value("Tipo de token inválido!"))
                .andExpect(jsonPath("$.code").value("AUTH_FAILED"));
    }

    @Test
    void deveRegistarUsuarioComSucesso() throws Exception {
        // Arrange
        var request = new AutenticacaoRequest("username", "password");
        var json = objectMapper.writeValueAsString(request);

        doNothing().when(autenticacaoService).registrar(any());

        // Act
        mockMvc.perform(post("/auth/register")
                        .contentType("application/json")
                        .content(json))
                .andExpect(status().isCreated());
    }

    private String gerarTokenComTipo(String tipo) {
        var algoritmo = Algorithm.HMAC256("12345678"); // use a mesma chave do seu profile dev
        return JWT.create()
                .withSubject("usuario")
                .withIssuer("API Stockify")
                .withClaim("type", tipo)
                .withExpiresAt(new Date(System.currentTimeMillis() + 1000 * 60 * 5)) // 5 min
                .sign(algoritmo);
    }

}
package com.eduardo.stockify.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class TokenServiceTest {

    private final String secret = "tokenSecret";

    @InjectMocks
    private TokenService tokenService = new TokenService(secret);

    @Test
    void deveGerarAccessTokenValido() {
        var token = tokenService.gerarAccessToken("usuario123");
        assertNotNull(token);
        assertTrue(token.startsWith("ey"));

        var subject = tokenService.getSubject(token, "access");
        assertEquals("usuario123", subject);
    }

    @Test
    void deveGerarUmRefreshTokenValido() {
        var token = tokenService.gerarRefreshToken("usuario123");
        assertNotNull(token);
        assertTrue(token.startsWith("ey"));

        var subject = tokenService.getSubject(token, "refresh");
        assertEquals("usuario123", subject);
    }

    @Test
    void deveLancarExcecao_QuandoTipoDeTokenForInvalido() {
        var refreshToken = tokenService.gerarRefreshToken("usuario123");

        var ex = assertThrows(RuntimeException.class, () -> {
            tokenService.getSubject(refreshToken, "access");
        });

        assertEquals("Tipo de token inválido!", ex.getMessage());
    }

    @Test
    void deveLancarExcecao_QuandoTokenForInvalidoOuExpirado() {
        var tokenInvalido = "abc.def.ghi";

        var ex = assertThrows(RuntimeException.class, () -> {
            tokenService.getSubject(tokenInvalido, "access");
        });

        assertEquals("Token JWT inválido ou expirado!", ex.getMessage());
    }

    @Test
    void deveLancarExcecao_QuandoSecretForVazio() {
        var serviceComErro = new TokenService("");

        var ex = assertThrows(IllegalArgumentException.class, () -> {
            serviceComErro.gerarAccessToken("usuario123");
        });

        assertEquals("Empty key", ex.getMessage());
    }

}
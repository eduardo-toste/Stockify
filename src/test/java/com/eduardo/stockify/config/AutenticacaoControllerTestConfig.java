package com.eduardo.stockify.config;

import com.eduardo.stockify.services.AutenticacaoService;
import com.eduardo.stockify.services.TokenService;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;

import static org.mockito.Mockito.mock;

@TestConfiguration
public class AutenticacaoControllerTestConfig {

    @Bean
    AuthenticationManager authenticationManager() {
        return mock(AuthenticationManager.class);
    }

    @Bean
    TokenService tokenService() {
        return mock(TokenService.class);
    }

    @Bean
    AutenticacaoService autenticacaoService() {
        return mock(AutenticacaoService.class);
    }
}

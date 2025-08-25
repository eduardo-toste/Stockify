package com.eduardo.stockify.config;

import com.eduardo.stockify.config.security.SecurityFilter;
import com.eduardo.stockify.repositories.UsuarioRepository;
import com.eduardo.stockify.services.TokenService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.PrintWriter;
import java.time.OffsetDateTime;
import java.util.Map;

@TestConfiguration
@RequiredArgsConstructor
public class TestSecurityConfig {

    private final TokenService tokenService;
    private final UsuarioRepository usuarioRepository;
    private final ObjectMapper objectMapper;

    @Bean
    public SecurityFilterChain testSecurityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/auth/**").permitAll()
                        .anyRequest().authenticated())
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint(restAuthenticationEntryPoint())
                        .accessDeniedHandler(restAccessDeniedHandler()))
                .addFilterBefore(new SecurityFilter(tokenService, usuarioRepository),
                        UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public AuthenticationEntryPoint restAuthenticationEntryPoint() {
        return (request, response, authException) -> {
            response.setStatus(401);
            response.setContentType("application/json");
            try (PrintWriter writer = response.getWriter()) {
                writer.write(objectMapper.writeValueAsString(Map.of(
                        "timestamp", OffsetDateTime.now().toString(),
                        "status", 401,
                        "error", "Unauthorized",
                        "message", "Token ausente ou inválido",
                        "path", request.getRequestURI()
                )));
            }
        };
    }

    @Bean
    public AccessDeniedHandler restAccessDeniedHandler() {
        return (request, response, accessDeniedException) -> {
            response.setStatus(403);
            response.setContentType("application/json");
            try (PrintWriter writer = response.getWriter()) {
                writer.write(objectMapper.writeValueAsString(Map.of(
                        "timestamp", OffsetDateTime.now().toString(),
                        "status", 403,
                        "error", "Forbidden",
                        "message", "Acesso negado",
                        "path", request.getRequestURI()
                )));
            }
        };
    }
}
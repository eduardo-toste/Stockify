package com.eduardo.stockify.config;

import com.eduardo.stockify.config.security.SecurityFilter;
import com.eduardo.stockify.services.TokenService;
import com.eduardo.stockify.repositories.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@TestConfiguration
@RequiredArgsConstructor
public class TestSecurityConfig {

    private final TokenService tokenService;
    private final UsuarioRepository usuarioRepository;

    @Bean
    public SecurityFilterChain testSecurityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/auth/**").permitAll()
                        .anyRequest().authenticated())
                .addFilterBefore(new SecurityFilter(tokenService, usuarioRepository),
                        org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter.class)
                .build();
    }
}
package com.eduardo.stockify.controllers;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.eduardo.stockify.dtos.AutenticacaoRequest;
import com.eduardo.stockify.dtos.RefreshTokenRequest;
import com.eduardo.stockify.dtos.TokenResponse;
import com.eduardo.stockify.models.Usuario;
import com.eduardo.stockify.services.AutenticacaoService;
import com.eduardo.stockify.services.TokenService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.antlr.v4.runtime.Token;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AutenticacaoController {

    private final AuthenticationManager manager;
    private final TokenService tokenService;
    private final AutenticacaoService service;

    @PostMapping("/login")
    public ResponseEntity<TokenResponse> login(@RequestBody @Valid AutenticacaoRequest dados){
        var auth = manager.authenticate(new UsernamePasswordAuthenticationToken(dados.username(), dados.password()));
        var user = (UserDetails) auth.getPrincipal();

        var accessToken = tokenService.gerarAccessToken(user.getUsername());
        var refreshToken = tokenService.gerarRefreshToken(user.getUsername());

        return ResponseEntity.ok(new TokenResponse(accessToken, refreshToken));
    }

    @PostMapping("/refresh")
    public ResponseEntity<TokenResponse> refresh(@RequestBody RefreshTokenRequest request) {
        if (request.refreshToken() == null || request.refreshToken().isBlank()) {
            throw new JWTVerificationException("Refresh token ausente");
        }

        String username = tokenService.getSubject(request.refreshToken(), "refresh");

        var newAccessToken = tokenService.gerarAccessToken(username);
        var newRefreshToken = tokenService.gerarRefreshToken(username);

        return ResponseEntity.ok(new TokenResponse(newAccessToken, newRefreshToken));
    }

    @PostMapping("/register")
    public ResponseEntity<Void> registrar(@RequestBody @Valid AutenticacaoRequest dados){
        service.registrar(dados);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

}
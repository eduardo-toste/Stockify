package com.eduardo.stockify.controllers;

import com.eduardo.stockify.dtos.AutenticacaoRequest;
import com.eduardo.stockify.dtos.TokenResponse;
import com.eduardo.stockify.models.Usuario;
import com.eduardo.stockify.services.AutenticacaoService;
import com.eduardo.stockify.services.TokenService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AutenticacaoController {

    @Autowired
    private AuthenticationManager manager;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private AutenticacaoService service;

    @PostMapping("/login")
    public ResponseEntity efetuarLogin(@RequestBody @Valid AutenticacaoRequest dados){
        var authenticationToken = new UsernamePasswordAuthenticationToken(dados.username(), dados.password());
        var authentication = manager.authenticate(authenticationToken);

        var tokenJWT = tokenService.gerarToken((Usuario) authentication.getPrincipal());

        return ResponseEntity.ok(new TokenResponse(tokenJWT));
    }

    @PostMapping("/register")
    public ResponseEntity registrar(@RequestBody @Valid AutenticacaoRequest dados){
        service.registrar(dados);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

}
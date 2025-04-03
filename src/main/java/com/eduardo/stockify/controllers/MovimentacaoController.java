package com.eduardo.stockify.controllers;

import com.eduardo.stockify.dtos.MovimentacaoRequest;
import com.eduardo.stockify.services.MovimentacaoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/movimentacao")
public class MovimentacaoController {

    @Autowired
    private MovimentacaoService service;

    @PostMapping
    public ResponseEntity movimentacao(@RequestBody @Valid MovimentacaoRequest dados){
        service.movimentacao(dados);
    }

}

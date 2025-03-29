package com.eduardo.stockify.controllers;

import com.eduardo.stockify.dtos.DadosAutenticacao;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/login")
public class UsuarioController {

    @PostMapping
    public void autenticar(@RequestBody @Valid DadosAutenticacao dados){
        System.out.println(dados);
    }

}
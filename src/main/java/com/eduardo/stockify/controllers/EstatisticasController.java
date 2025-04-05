package com.eduardo.stockify.controllers;

import com.eduardo.stockify.dtos.EstatisticasResponse;
import com.eduardo.stockify.services.EstatisticasService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/estatisticas")
public class EstatisticasController {

    @Autowired
    private EstatisticasService service;

    @GetMapping
    public ResponseEntity<EstatisticasResponse> obterEstatisticas(){
        var estatisticas = service.estatisticas();

        return ResponseEntity.status(HttpStatus.OK).body(estatisticas);
    }

}

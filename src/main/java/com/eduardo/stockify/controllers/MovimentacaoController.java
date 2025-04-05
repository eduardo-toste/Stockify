package com.eduardo.stockify.controllers;

import com.eduardo.stockify.dtos.MovimentacaoRequest;
import com.eduardo.stockify.dtos.MovimentacaoResponse;
import com.eduardo.stockify.services.MovimentacaoService;
import com.eduardo.stockify.services.MovimentacoesExportService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/movimentacao")
public class MovimentacaoController {

    @Autowired
    private MovimentacaoService service;

    @Autowired
    private MovimentacoesExportService exportService;

    @PostMapping
    public ResponseEntity<MovimentacaoResponse> movimentacao(@RequestBody @Valid MovimentacaoRequest dados){
        var movimentacao = service.movimentacao(dados);

        return ResponseEntity.status(HttpStatus.CREATED).body(movimentacao);
    }

    @GetMapping
    public ResponseEntity<List<MovimentacaoResponse>> listar(){
        var listaMovimentacoes = service.listarMovimentacoes();

        return ResponseEntity.status(HttpStatus.OK).body(listaMovimentacoes);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MovimentacaoResponse> listarPorId(@PathVariable Long id){
        var movimentacao = service.listarMovimentacoesPorId(id);

        return ResponseEntity.status(HttpStatus.OK).body(movimentacao);
    }

    @GetMapping("/exportar")
    public ResponseEntity<byte[]> exportar() throws IOException {
        byte[] excelBytes = exportService.export("Movimentações");

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"movimentações.xlsx\"")
                .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                .body(excelBytes);
    }


}

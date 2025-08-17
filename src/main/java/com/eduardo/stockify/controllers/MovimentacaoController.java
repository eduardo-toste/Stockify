package com.eduardo.stockify.controllers;

import com.eduardo.stockify.dtos.MovimentacaoRequest;
import com.eduardo.stockify.dtos.MovimentacaoResponse;
import com.eduardo.stockify.services.MovimentacaoService;
import com.eduardo.stockify.services.MovimentacoesExportService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/movimentacao")
@RequiredArgsConstructor
public class MovimentacaoController {

    private final MovimentacaoService service;
    private final MovimentacoesExportService exportService;

    @PostMapping
    public ResponseEntity<MovimentacaoResponse> movimentacao(@RequestBody @Valid MovimentacaoRequest dados){
        var movimentacao = service.criarMovimentacao(dados);

        return ResponseEntity.status(HttpStatus.CREATED).body(movimentacao);
    }

    @GetMapping
    public ResponseEntity<Page<MovimentacaoResponse>> listar(Pageable pageable){
        var listaMovimentacoes = service.listarMovimentacoes(pageable);

        return ResponseEntity.status(HttpStatus.OK).body(listaMovimentacoes);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MovimentacaoResponse> listarPorId(@PathVariable Long id){
        var movimentacao = service.listarMovimentacoesPorId(id);

        return ResponseEntity.status(HttpStatus.OK).body(movimentacao);
    }

    @GetMapping("/exportar")
    public ResponseEntity<Map<String, String>> exportar() throws IOException {
        Map<String, String> response = exportService.enviarParaS3("Movimentacoes");

        return ResponseEntity.ok(response);
    }


}

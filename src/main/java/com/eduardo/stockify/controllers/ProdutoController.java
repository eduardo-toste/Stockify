package com.eduardo.stockify.controllers;

import com.eduardo.stockify.dtos.ProdutoRequest;
import com.eduardo.stockify.dtos.ProdutoResponse;
import com.eduardo.stockify.dtos.ProdutoUpdateResponse;
import com.eduardo.stockify.services.ProdutoExportService;
import com.eduardo.stockify.services.ProdutoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/produtos")
@RequiredArgsConstructor
public class ProdutoController {

    private final ProdutoService service;
    private final ProdutoExportService exportService;

    @PostMapping
    public ResponseEntity<ProdutoResponse> cadastrar(@RequestBody @Valid ProdutoRequest dados){
        var produtoCriado = service.criarProduto(dados);
        return ResponseEntity.status(HttpStatus.CREATED).body(produtoCriado);
    }

    @GetMapping
    public ResponseEntity<Page<ProdutoResponse>> listar(Pageable pageable){
        var produtos = service.listarProdutos(pageable);
        return ResponseEntity.status(HttpStatus.OK).body(produtos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProdutoResponse> listarPorId(@PathVariable Long id){
        var produtos = service.listarProdutoPorId(id);
        return ResponseEntity.status(HttpStatus.OK).body(produtos);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deletar(@PathVariable Long id){
        service.excluirProduto(id);

        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProdutoUpdateResponse> alterar(@PathVariable Long id, @RequestBody @Valid ProdutoRequest dados){
        var produtoAlterado = service.alterarProduto(id, dados);

        return ResponseEntity.status(HttpStatus.OK).body(produtoAlterado);
    }

    @GetMapping("/exportar")
    public ResponseEntity<Map<String, String>> exportar() throws IOException {
        Map<String, String> response = exportService.enviarParaS3("Produtos");

        return ResponseEntity.ok(response);
    }

}

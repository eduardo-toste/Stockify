package com.eduardo.stockify.controllers;

import com.eduardo.stockify.dtos.ProdutoRequest;
import com.eduardo.stockify.dtos.ProdutoResponse;
import com.eduardo.stockify.services.ProdutoExportService;
import com.eduardo.stockify.services.ProdutoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/produtos")
public class ProdutoController {

    @Autowired
    private ProdutoService service;

    @Autowired
    private ProdutoExportService exportService;

    @PostMapping
    public ResponseEntity<ProdutoResponse> cadastrar(@RequestBody @Valid ProdutoRequest dados){
        var produtoCriado = service.criarProduto(dados);
        return ResponseEntity.status(HttpStatus.CREATED).body(produtoCriado);
    }

    @GetMapping
    public ResponseEntity<List<ProdutoResponse>> listar(){
        var produtos = service.listarProdutos();
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
    public ResponseEntity<ProdutoResponse> alterar(@PathVariable Long id, @RequestBody @Valid ProdutoRequest dados){
        var produtoAlterado = service.alterarProduto(id, dados);

        return ResponseEntity.status(HttpStatus.OK).body(produtoAlterado);
    }

    @GetMapping("/exportar")
    public ResponseEntity<byte[]> exportar() throws IOException {
        byte[] excelBytes = exportService.export("Produtos");

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"produtos.xlsx\"")
                .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                .body(excelBytes);
    }

}

package com.eduardo.stockify.controllers;

import com.eduardo.stockify.dtos.ProdutoRequest;
import com.eduardo.stockify.dtos.ProdutoResponse;
import com.eduardo.stockify.services.ProdutoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/produtos")
public class ProdutoController {

    @Autowired
    private ProdutoService service;

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
}

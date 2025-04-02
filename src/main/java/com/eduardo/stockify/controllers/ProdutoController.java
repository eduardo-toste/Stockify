package com.eduardo.stockify.controllers;

import com.eduardo.stockify.dtos.DadosCadastroProduto;
import com.eduardo.stockify.dtos.DadosDetalhamentoProduto;
import com.eduardo.stockify.models.Produto;
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
    public ResponseEntity<DadosDetalhamentoProduto> cadastrar(@RequestBody @Valid DadosCadastroProduto dados){
        var produtoCriado = service.criarProduto(dados);
        return ResponseEntity.status(HttpStatus.CREATED).body(produtoCriado);
    }

    @GetMapping
    public ResponseEntity<List<DadosDetalhamentoProduto>> listar(){
        var produtos = service.listarProdutos();
        return ResponseEntity.status(HttpStatus.OK).body(produtos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DadosDetalhamentoProduto> listarPorId(@PathVariable Long id){
        var produtos = service.listarProdutoPorId(id);
        return ResponseEntity.status(HttpStatus.OK).body(produtos);
    }
}

package com.eduardo.stockify.services;

import com.eduardo.stockify.dtos.DadosCadastroProduto;
import com.eduardo.stockify.dtos.DadosDetalhamentoProduto;
import com.eduardo.stockify.models.Produto;
import com.eduardo.stockify.repositories.ProdutoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class ProdutoService {

    @Autowired
    private ProdutoRepository repository;

    public DadosDetalhamentoProduto criarProduto(DadosCadastroProduto dados){
        if(repository.existsByNome(dados.nome())){
            throw new RuntimeException("Já existe um produto cadastrado com este nome");
        }

        var produto = new Produto(
                null,
                dados.nome(),
                dados.descricao(),
                dados.preco(),
                dados.quantidade(),
                dados.categoria(),
                LocalDateTime.now()
        );

        var produtoSalvo = repository.save(produto);

        return new DadosDetalhamentoProduto(produtoSalvo);
    }
}

package com.eduardo.stockify.services;

import com.eduardo.stockify.dtos.ProdutoRequest;
import com.eduardo.stockify.dtos.ProdutoResponse;
import com.eduardo.stockify.exceptions.EstoqueVazioException;
import com.eduardo.stockify.exceptions.ProdutoNotFoundException;
import com.eduardo.stockify.models.Produto;
import com.eduardo.stockify.repositories.ProdutoRepository;
import com.eduardo.stockify.services.validacoes.Validacao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ProdutoService {

    @Autowired
    private ProdutoRepository repository;

    @Autowired
    private List<Validacao> validacao;

    public ProdutoResponse criarProduto(ProdutoRequest dados){
        validacao.forEach(v -> v.validar(dados));

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

        return new ProdutoResponse(produtoSalvo);
    }

    public List<ProdutoResponse> listarProdutos(){
        var produtos = repository.findAll();

        if(produtos.isEmpty()){
            throw new EstoqueVazioException("Não existem produtos cadastrados!");
        }

        return produtos.stream()
                .map(ProdutoResponse::new)
                .toList();
    }

    public ProdutoResponse listarProdutoPorId(Long id) {
        var produto = repository.findById(id);

        var produtoEncontrado = produto.map(ProdutoResponse::new)
                .orElseThrow(() -> new ProdutoNotFoundException("Produto não encontrado!"));

        return produtoEncontrado;
    }

    public void excluirProduto(Long id) {
        repository.deleteById(id);
    }
}

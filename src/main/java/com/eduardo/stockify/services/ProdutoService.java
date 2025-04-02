package com.eduardo.stockify.services;

import com.eduardo.stockify.dtos.ProdutoRequest;
import com.eduardo.stockify.dtos.ProdutoResponse;
import com.eduardo.stockify.exceptions.EstoqueVazioException;
import com.eduardo.stockify.models.Produto;
import com.eduardo.stockify.repositories.ProdutoRepository;
import com.eduardo.stockify.services.validations.ValidacaoEspecifica;
import com.eduardo.stockify.services.validations.ValidacaoGeral;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ProdutoService {

    @Autowired
    private ProdutoRepository repository;

    @Autowired
    private List<ValidacaoGeral> validacaoGeral;

    @Autowired
    private List<ValidacaoEspecifica> validacaoEspecifica;

    public ProdutoResponse criarProduto(ProdutoRequest dados){
        validacaoGeral.forEach(v -> v.validar(dados));

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
        validacaoEspecifica.forEach(v -> v.validar(id));

        var produto = repository.findById(id);

        return produto.map(ProdutoResponse::new).get();
    }

    public void excluirProduto(Long id) {
        validacaoEspecifica.forEach(v -> v.validar(id));

        repository.deleteById(id);
    }
}

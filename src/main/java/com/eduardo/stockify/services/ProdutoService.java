package com.eduardo.stockify.services;

import com.eduardo.stockify.dtos.DadosCadastroProduto;
import com.eduardo.stockify.dtos.DadosDetalhamentoProduto;
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

    public DadosDetalhamentoProduto criarProduto(DadosCadastroProduto dados){
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

        return new DadosDetalhamentoProduto(produtoSalvo);
    }

    public List<Produto> listarProdutos(){
        return repository.findAll();
    }
}

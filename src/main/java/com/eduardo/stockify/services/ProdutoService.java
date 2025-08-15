package com.eduardo.stockify.services;

import com.eduardo.stockify.dtos.ProdutoRequest;
import com.eduardo.stockify.dtos.ProdutoResponse;
import com.eduardo.stockify.exceptions.TransactionFailedException;
import com.eduardo.stockify.exceptions.EstoqueVazioException;
import com.eduardo.stockify.models.Produto;
import com.eduardo.stockify.repositories.ProdutoRepository;
import com.eduardo.stockify.services.validations.ValidacaoEspecifica;
import com.eduardo.stockify.services.validations.ValidacaoGeral;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProdutoService {

    private ProdutoRepository repository;
    private List<ValidacaoGeral> validacaoGeral;
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

    public Page<ProdutoResponse> listarProdutos(Pageable pageable){
        var produtos = repository.findAll(pageable);

        if(produtos.isEmpty()){
            throw new EstoqueVazioException("Não existem produtos cadastrados!");
        }

        return produtos.map(ProdutoResponse::new);
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

    @Transactional
    public ProdutoResponse alterarProduto(Long id, ProdutoRequest dados) {
        validacaoEspecifica.forEach(v -> v.validar(id));
        validacaoGeral.forEach(v -> v.validar(dados));

        int linhasAfetadas = repository.atualizarProduto(
                id,
                dados.nome(),
                dados.descricao(),
                dados.preco(),
                dados.quantidade(),
                dados.categoria()
        );

        if(linhasAfetadas == 0){
            throw new TransactionFailedException("A alteração do registro falhou!");
        }

        var produtoAtualizado = repository.findById(id).get();

        return new ProdutoResponse(produtoAtualizado);
    }
}

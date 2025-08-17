package com.eduardo.stockify.services;

import com.eduardo.stockify.dtos.ProdutoRequest;
import com.eduardo.stockify.dtos.ProdutoResponse;
import com.eduardo.stockify.dtos.ProdutoUpdateResponse;
import com.eduardo.stockify.exceptions.ProdutoExistenteException;
import com.eduardo.stockify.exceptions.ResourceNotFoundException;
import com.eduardo.stockify.exceptions.TransactionFailedException;
import com.eduardo.stockify.mapper.ProdutoMapper;
import com.eduardo.stockify.models.Produto;
import com.eduardo.stockify.repositories.ProdutoRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProdutoService {

    private final ProdutoRepository repository;

    public ProdutoResponse criarProduto(ProdutoRequest dados){
        verificarExistenciaProduto(dados);
        var produto = ProdutoMapper.toEntity(dados);
        repository.save(produto);
        return ProdutoMapper.toDTO(produto);
    }

    public Page<ProdutoResponse> listarProdutos(Pageable pageable){
        var produtos = repository.findAll(pageable);
        return ProdutoMapper.toPageDTO(produtos);
    }

    public ProdutoResponse listarProdutoPorId(Long id) {;
        return ProdutoMapper.toDTO(buscarProdutoPorId(id));
    }

    public void excluirProduto(Long id) {
        buscarProdutoPorId(id);
        repository.deleteById(id);
    }

    @Transactional
    public ProdutoUpdateResponse alterarProduto(Long id, ProdutoRequest dados) {
        buscarProdutoPorId(id);
        var produtoAtualizado = ProdutoMapper.toEntity(id, dados);

        int linhasAfetadas = repository.atualizarProduto(produtoAtualizado);
        if(linhasAfetadas == 0){
            throw new TransactionFailedException("A alteração falhou");
        }

        return ProdutoMapper.toUpdateDTO(produtoAtualizado);
    }

    private void verificarExistenciaProduto(ProdutoRequest request) {
        if(repository.existsByNome(request.nome())){
            throw new ProdutoExistenteException("Produto já cadastrado com este nome");
        }
    }

    private Produto buscarProdutoPorId(Long id) {
        return repository.findById(id).
                orElseThrow(() -> new ResourceNotFoundException("Produto não encontrado"));
    }
}

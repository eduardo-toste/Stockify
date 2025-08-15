package com.eduardo.stockify.services;

import com.eduardo.stockify.dtos.MovimentacaoRequest;
import com.eduardo.stockify.dtos.MovimentacaoResponse;
import com.eduardo.stockify.exceptions.EstoqueInsuficienteException;
import com.eduardo.stockify.exceptions.ProdutoNotFoundException;
import com.eduardo.stockify.exceptions.TransactionFailedException;
import com.eduardo.stockify.exceptions.ResourceNotFoundException;
import com.eduardo.stockify.mapper.MovimentacaoMapper;
import com.eduardo.stockify.models.enums.TipoMovimentacao;
import com.eduardo.stockify.repositories.MovimentacaoRepository;
import com.eduardo.stockify.repositories.ProdutoRepository;
import com.eduardo.stockify.services.validations.ValidacaoEspecifica;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MovimentacaoService {

    private final MovimentacaoRepository movimentacaoRepository;
    private final ProdutoRepository produtoRepository;

    @Transactional
    public MovimentacaoResponse criarMovimentacao(MovimentacaoRequest dados) {
        if(!produtoRepository.existsById(dados.produtoId())){
            throw new ResourceNotFoundException("Produto não encontrado");
        }

        var produto = produtoRepository.getReferenceById(dados.produtoId());
        alterarQuantidade(dados.produtoId(), dados.quantidade(), dados.tipo());

        var movimentacao = MovimentacaoMapper.toEntity(produto, dados);
        movimentacaoRepository.save(movimentacao);
        return MovimentacaoMapper.toDTO(movimentacao);
    }

    @Transactional
    public void alterarQuantidade(Long produtoId, int quantidadeMovimentada, TipoMovimentacao tipo) {
        int delta = (tipo == TipoMovimentacao.ENTRADA) ? quantidadeMovimentada : -quantidadeMovimentada;

        int rows = produtoRepository.aplicarDeltaDeEstoque(produtoId, delta);
        if (rows == 1) return;

        if (produtoRepository.existsById(produtoId)) {
            throw new EstoqueInsuficienteException("Estoque insuficiente para a movimentação solicitada.");
        }
        throw new ResourceNotFoundException("Produto %d não encontrado.".formatted(produtoId));
    }

    public Page<MovimentacaoResponse> listarMovimentacoes(Pageable pageable) {
        var movimentacoes = movimentacaoRepository.findAll(pageable);
        return MovimentacaoMapper.toPageDTO(movimentacoes);
    }

    public MovimentacaoResponse listarMovimentacoesPorId(Long id) {
        var movimentacao = movimentacaoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Movimentação não encontrada"));
        return MovimentacaoMapper.toDTO(movimentacao);
    }
}

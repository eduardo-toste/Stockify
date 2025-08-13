package com.eduardo.stockify.services;

import com.eduardo.stockify.dtos.MovimentacaoRequest;
import com.eduardo.stockify.dtos.MovimentacaoResponse;
import com.eduardo.stockify.dtos.ProdutoResponse;
import com.eduardo.stockify.exceptions.AlteracaoFalhouException;
import com.eduardo.stockify.exceptions.EstoqueVazioException;
import com.eduardo.stockify.models.Movimentacao;
import com.eduardo.stockify.models.enums.TipoMovimentacao;
import com.eduardo.stockify.repositories.MovimentacaoRepository;
import com.eduardo.stockify.repositories.ProdutoRepository;
import com.eduardo.stockify.services.validations.ValidacaoEspecifica;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class MovimentacaoService {

    @Autowired
    private MovimentacaoRepository movimentacaoRepository;

    @Autowired
    private ProdutoRepository produtoRepository;

    @Autowired
    private List<ValidacaoEspecifica> validacaoEspecifica;

    public MovimentacaoResponse movimentacao(MovimentacaoRequest dados) {
        validacaoEspecifica.forEach(v -> v.validar(dados.produtoId()));
        var produto = produtoRepository.getReferenceById(dados.produtoId());

        int linhasAfetadas = alterarQuantidade(dados.produtoId(), dados.quantidade(), dados.tipo());

        if (linhasAfetadas == 0) {
            throw new AlteracaoFalhouException("A movimentação do produto falhou!");
        }

        var movimentacao = new Movimentacao(
                null,
                produto,
                dados.tipo(),
                dados.quantidade(),
                LocalDateTime.now()
        );

        var movimentacaoSalva = movimentacaoRepository.save(movimentacao);

        return new MovimentacaoResponse(movimentacaoSalva);
    }

    @Transactional
    public int alterarQuantidade(Long produtoId, int quantidadeMovimentada, TipoMovimentacao tipo) {
        var quantidadeAtual = produtoRepository.verificarQuantidade(produtoId);
        int quantidadeAtualizada;

        if (tipo.equals(TipoMovimentacao.ENTRADA)) {
            quantidadeAtualizada = quantidadeAtual + quantidadeMovimentada;
        } else {
            if (quantidadeMovimentada > quantidadeAtual) {
                throw new RuntimeException("A quantidade da movimentação não pode ser maior que a quantidade em estoque!");
            }
            quantidadeAtualizada = quantidadeAtual - quantidadeMovimentada;
        }

        return produtoRepository.alterarQuantidade(produtoId, quantidadeAtualizada);
    }

    public Page<MovimentacaoResponse> listarMovimentacoes(Pageable pageable) {
        var movimentacoes = movimentacaoRepository.findAll(pageable);

        if(movimentacoes.isEmpty()){
            throw new EstoqueVazioException("Não existem movimentações!");
        }

        return movimentacoes.map(MovimentacaoResponse::new);
    }

    public MovimentacaoResponse listarMovimentacoesPorId(Long id) {
        validacaoEspecifica.forEach(v -> v.validar(id));

        var movimentacao = movimentacaoRepository.findById(id);

        return movimentacao.map(MovimentacaoResponse::new).get();
    }
}

package com.eduardo.stockify.dtos;

import com.eduardo.stockify.models.Movimentacao;
import com.eduardo.stockify.models.enums.TipoMovimentacao;

import java.time.LocalDateTime;

public record MovimentacaoResponse(Long id, Long produtoId, TipoMovimentacao tipoMovimentacao, int quantidade, LocalDateTime data) {

    public MovimentacaoResponse(Movimentacao movimentacao){
        this(movimentacao.getId(), movimentacao.getProdutoId(), movimentacao.getTipo(), movimentacao.getQuantidade(), movimentacao.getData());
    }

}

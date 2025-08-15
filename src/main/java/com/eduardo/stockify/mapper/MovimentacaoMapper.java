package com.eduardo.stockify.mapper;

import com.eduardo.stockify.dtos.MovimentacaoRequest;
import com.eduardo.stockify.dtos.MovimentacaoResponse;
import com.eduardo.stockify.models.Movimentacao;
import com.eduardo.stockify.models.Produto;
import org.springframework.data.domain.Page;

import java.time.LocalDateTime;

public class MovimentacaoMapper {

    public static Movimentacao toEntity(Produto produto, MovimentacaoRequest request) {
        return new Movimentacao(
                null,
                produto,
                request.tipo(),
                request.quantidade(),
                LocalDateTime.now()
        );
    }

    public static MovimentacaoResponse toDTO(Movimentacao movimentacao) {
        return new MovimentacaoResponse(
                movimentacao.getId(),
                movimentacao.getProduto().getId(),
                movimentacao.getTipo(),
                movimentacao.getQuantidade(),
                movimentacao.getData()
        );
    }

    public static Page<MovimentacaoResponse> toPageDTO(Page<Movimentacao> page) {
        return page.map(MovimentacaoMapper::toDTO);
    }

}

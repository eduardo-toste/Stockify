package com.eduardo.stockify.dtos;

import com.eduardo.stockify.models.enums.TipoMovimentacao;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record MovimentacaoRequest(

        @NotNull
        Long produtoId,

        @NotNull
        TipoMovimentacao tipo,

        @NotNull
        @Min(1)
        int quantidade

) {
}

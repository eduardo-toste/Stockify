package com.eduardo.stockify.dtos;

import com.eduardo.stockify.models.enums.TipoMovimentacao;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public record MovimentacaoRequest(

        @NotBlank
        Long produtoId,

        @NotBlank
        TipoMovimentacao tipo,

        @Min(0)
        int quantidade

) {
}

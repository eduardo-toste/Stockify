package com.eduardo.stockify.services.validacoes;

import com.eduardo.stockify.dtos.ProdutoRequest;

public interface ValidacaoGeral {

    void validar(ProdutoRequest request);

}

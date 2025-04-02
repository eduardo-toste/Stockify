package com.eduardo.stockify.services.validations;

import com.eduardo.stockify.dtos.ProdutoRequest;

public interface ValidacaoGeral {

    void validar(ProdutoRequest request);

}

package com.eduardo.stockify.services.validacoes;

import com.eduardo.stockify.dtos.ProdutoRequest;

public interface Validacao {

    void validar(ProdutoRequest request);

}

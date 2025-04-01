package com.eduardo.stockify.services.validacoes;

import com.eduardo.stockify.dtos.DadosCadastroProduto;

public interface Validacao {

    void validar(DadosCadastroProduto request);

}

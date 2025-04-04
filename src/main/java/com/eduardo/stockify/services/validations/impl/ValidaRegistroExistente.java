package com.eduardo.stockify.services.validations.impl;

import com.eduardo.stockify.dtos.ProdutoRequest;
import com.eduardo.stockify.exceptions.ProdutoExistenteException;
import com.eduardo.stockify.repositories.ProdutoRepository;
import com.eduardo.stockify.services.validations.ValidacaoGeral;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ValidaRegistroExistente implements ValidacaoGeral {

    @Autowired
    ProdutoRepository repository;

    @Override
    public void validar(ProdutoRequest request) {
        if(repository.existsByNome(request.nome())){
            throw new ProdutoExistenteException("Produto já cadastrado com este nome!");
        }
    }

}

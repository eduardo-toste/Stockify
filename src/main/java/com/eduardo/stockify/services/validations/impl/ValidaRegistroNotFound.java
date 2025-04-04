package com.eduardo.stockify.services.validations.impl;

import com.eduardo.stockify.exceptions.ProdutoNotFoundException;
import com.eduardo.stockify.repositories.ProdutoRepository;
import com.eduardo.stockify.services.validations.ValidacaoEspecifica;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ValidaRegistroNotFound implements ValidacaoEspecifica {

    @Autowired
    private ProdutoRepository repository;

    @Override
    public void validar(Long id) {
        if(!repository.existsById(id)){
            throw new ProdutoNotFoundException("Registro não encontrado no estoque!");
        }
    }

}

package com.eduardo.stockify.services;

import com.eduardo.stockify.dtos.EstatisticasResponse;
import com.eduardo.stockify.models.Produto;
import com.eduardo.stockify.repositories.ProdutoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EstatisticasService {

    @Autowired
    private ProdutoRepository repository;

    public EstatisticasResponse estatisticas() {
        var produtos = repository.findAll();

        var statsPreco = produtos.stream()
                .mapToDouble(Produto::getPreco)
                .summaryStatistics();

        var statsQuantidade = produtos.stream()
                .mapToInt(Produto::getQuantidade)
                .summaryStatistics();

        var response = new EstatisticasResponse(
                statsPreco.getCount(),
                statsQuantidade.getSum(),
                statsPreco.getSum(),
                statsPreco.getAverage(),
                statsPreco.getMin(),
                statsPreco.getMax()
        );

        return response;
    }
}

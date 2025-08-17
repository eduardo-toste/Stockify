package com.eduardo.stockify.services;

import com.eduardo.stockify.dtos.EstatisticasResponse;
import com.eduardo.stockify.mapper.EstatisticasMapper;
import com.eduardo.stockify.models.Produto;
import com.eduardo.stockify.repositories.ProdutoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.DoubleSummaryStatistics;
import java.util.IntSummaryStatistics;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EstatisticasService {

    private final ProdutoRepository repository;

    public EstatisticasResponse estatisticas() {
        var produtos = repository.findAll();
        var statsPreco = getStatsPreco(produtos);
        var statsQuantidade = getStatsQuantidade(produtos);

        return EstatisticasMapper.toDTO(statsPreco, statsQuantidade);
    }

    private DoubleSummaryStatistics getStatsPreco(List<Produto> produtos) {
        return produtos.stream()
                .mapToDouble(Produto::getPreco)
                .summaryStatistics();
    }

    private IntSummaryStatistics getStatsQuantidade(List<Produto> produtos) {
        return produtos.stream()
                .mapToInt(Produto::getQuantidade)
                .summaryStatistics();
    }
}

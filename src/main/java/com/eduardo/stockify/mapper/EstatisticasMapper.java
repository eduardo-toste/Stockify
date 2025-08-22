package com.eduardo.stockify.mapper;


import com.eduardo.stockify.dtos.EstatisticasResponse;

import java.util.DoubleSummaryStatistics;
import java.util.IntSummaryStatistics;

public class EstatisticasMapper {

    public static EstatisticasResponse toDTO(
            DoubleSummaryStatistics statsPreco,
            IntSummaryStatistics statsQuantidade) {

        double media = statsPreco.getCount() == 0 ? 0.0 : statsPreco.getAverage();
        double min = statsPreco.getCount() == 0 ? 0.0 : statsPreco.getMin();
        double max = statsPreco.getCount() == 0 ? 0.0 : statsPreco.getMax();

        return new EstatisticasResponse(
                statsPreco.getCount(),
                statsQuantidade.getSum(),
                statsPreco.getSum(),
                media,
                min,
                max
        );
    }

}

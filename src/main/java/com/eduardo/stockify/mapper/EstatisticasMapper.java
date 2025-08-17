package com.eduardo.stockify.mapper;


import com.eduardo.stockify.dtos.EstatisticasResponse;

import java.util.DoubleSummaryStatistics;
import java.util.IntSummaryStatistics;

public class EstatisticasMapper {

    public static EstatisticasResponse toDTO(
            DoubleSummaryStatistics statsPreco,
            IntSummaryStatistics statsQuantidade) {

        return new EstatisticasResponse(
                statsPreco.getCount(),
                statsQuantidade.getSum(),
                statsPreco.getSum(),
                statsPreco.getAverage(),
                statsPreco.getMin(),
                statsPreco.getMax()
        );
    }

}

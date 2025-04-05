package com.eduardo.stockify.dtos;

public record EstatisticasResponse (

        long produtos,
        long qtdUnidades,
        double valorTotal,
        double mediaPrecos,
        double precoMinimo,
        double precoMaximo

) {
}

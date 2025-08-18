package com.eduardo.stockify.services;

import com.eduardo.stockify.dtos.EstatisticasResponse;
import com.eduardo.stockify.models.Produto;
import com.eduardo.stockify.models.enums.Categoria;
import com.eduardo.stockify.repositories.ProdutoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EstatisticasServiceTest {

    @InjectMocks
    private EstatisticasService estatisticasService;

    @Mock
    private ProdutoRepository produtoRepository;

    @Test
    void deveRetornarEstatisticasCalculadas() {
        // Arrange
        var produto1 = new Produto(1L, "Produto A", "Descrição", 10.0, 2, Categoria.OUTROS);
        var produto2 = new Produto(2L, "Produto B", "Descrição", 40.0, 2, Categoria.OUTROS);
        var valorEsperado = new EstatisticasResponse(2L, 4L, 50.0, 25.0, 10.0, 40.0);
        when(produtoRepository.findAll()).thenReturn(List.of(produto1, produto2));

        // Act
        var result = estatisticasService.estatisticas();

        // Assert
        assertEquals(valorEsperado.produtos(), result.produtos());
        assertEquals(valorEsperado.qtdUnidades(), result.qtdUnidades());
        assertEquals(valorEsperado.valorTotal(), result.valorTotal());
        assertEquals(valorEsperado.mediaPrecos(), result.mediaPrecos());
        assertEquals(valorEsperado.precoMinimo(), result.precoMinimo());
        assertEquals(valorEsperado.precoMaximo(), result.precoMaximo());
    }

    @Test
    void deveRetornarEstatisticasZeradas() {
        // Arrange
        var valorEsperado = new EstatisticasResponse(0L, 0L, 0.0, 0.0, 0.0, 0.0);

        // Act
        var result = estatisticasService.estatisticas();

        // Assert
        assertEquals(valorEsperado.produtos(), result.produtos());
        assertEquals(valorEsperado.qtdUnidades(), result.qtdUnidades());
        assertEquals(valorEsperado.valorTotal(), result.valorTotal());
        assertEquals(valorEsperado.mediaPrecos(), result.mediaPrecos());
        assertEquals(valorEsperado.precoMinimo(), result.precoMinimo());
        assertEquals(valorEsperado.precoMaximo(), result.precoMaximo());
    }
}
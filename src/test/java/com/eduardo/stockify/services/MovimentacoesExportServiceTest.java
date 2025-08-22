package com.eduardo.stockify.services;

import com.eduardo.stockify.aws.s3.S3Service;
import com.eduardo.stockify.models.Movimentacao;
import com.eduardo.stockify.models.Produto;
import com.eduardo.stockify.models.enums.TipoMovimentacao;
import com.eduardo.stockify.repositories.MovimentacaoRepository;
import com.eduardo.stockify.utils.MovimentacaoTestUtils;
import com.eduardo.stockify.utils.ProdutoTestUtils;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MovimentacoesExportServiceTest {

    @Mock
    private MovimentacaoRepository repository;

    @Mock
    private S3Service s3Service;

    @InjectMocks
    private MovimentacoesExportService exportService;

    @Test
    void deveCriarCabecalhoCorretamente() {
        // Arrange
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet();

        // Act
        exportService.createHeader(sheet);
        Row header = sheet.getRow(0);

        // Assert
        assertEquals("ID", header.getCell(0).getStringCellValue());
        assertEquals("Produto", header.getCell(1).getStringCellValue());
        assertEquals("Tipo", header.getCell(2).getStringCellValue());
        assertEquals("Quantidade", header.getCell(3).getStringCellValue());
        assertEquals("Data", header.getCell(4).getStringCellValue());
    }

    @Test
    void devePreencherCorpoComDadosDaMovimentacao() {
        // Arrange
        Movimentacao m1 = MovimentacaoTestUtils.criarMovimentacaoPadrao();

        when(repository.findAll()).thenReturn(List.of(m1));

        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet();

        // Act
        exportService.createBody(sheet);
        Row row = sheet.getRow(1);

        // Assert
        assertEquals(1L, (long) row.getCell(0).getNumericCellValue());
        assertEquals("Produto Teste", row.getCell(1).getStringCellValue());
        assertEquals("ENTRADA", row.getCell(2).getStringCellValue());
        assertEquals(1, (int) row.getCell(3).getNumericCellValue());
        assertNotNull(row.getCell(4).getStringCellValue());
    }
}
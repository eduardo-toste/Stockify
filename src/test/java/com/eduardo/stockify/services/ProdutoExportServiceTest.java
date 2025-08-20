package com.eduardo.stockify.services;

import com.eduardo.stockify.aws.s3.S3Service;
import com.eduardo.stockify.models.Movimentacao;
import com.eduardo.stockify.models.Produto;
import com.eduardo.stockify.models.enums.Categoria;
import com.eduardo.stockify.repositories.MovimentacaoRepository;
import com.eduardo.stockify.repositories.ProdutoRepository;
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

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProdutoExportServiceTest {

    @InjectMocks
    private ProdutoExportService exportService;

    @Mock
    private ProdutoRepository repository;

    @Mock
    private S3Service s3Service;

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
        assertEquals("Nome", header.getCell(1).getStringCellValue());
        assertEquals("Descrição", header.getCell(2).getStringCellValue());
        assertEquals("Preço", header.getCell(3).getStringCellValue());
        assertEquals("Quantidade", header.getCell(4).getStringCellValue());
        assertEquals("Categoria", header.getCell(5).getStringCellValue());
        assertEquals("Data de Cadastro", header.getCell(6).getStringCellValue());
    }

    @Test
    void devePreencherCorpoComDadosDosProdutos() {
        // Arrange
        Produto produto = ProdutoTestUtils.criarProdutoPadrao();

        when(repository.findAll()).thenReturn(List.of(produto));

        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet();

        // Act
        exportService.createBody(sheet);
        Row row = sheet.getRow(1);

        // Assert
        assertEquals(1L, (long) row.getCell(0).getNumericCellValue());
        assertEquals("Produto Teste", row.getCell(1).getStringCellValue());
        assertEquals("Descrição", row.getCell(2).getStringCellValue());
        assertEquals(30.0, (int) row.getCell(3).getNumericCellValue());
        assertEquals(5, (int) row.getCell(4).getNumericCellValue());
        assertEquals("OUTROS", row.getCell(5).getStringCellValue());
        assertNotNull(row.getCell(6).getStringCellValue());
    }
}
package com.eduardo.stockify.services;

import com.eduardo.stockify.models.Produto;
import com.eduardo.stockify.repositories.ProdutoRepository;
import com.eduardo.stockify.utils.ExcelExporter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProdutoExportService extends ExcelExporter<Produto> {

    @Autowired
    private ProdutoRepository repository;

    @Override
    protected void createHeader(Sheet sheet) {
        Row header = sheet.createRow(0);
        header.createCell(0).setCellValue("ID");
        header.createCell(1).setCellValue("Nome");
        header.createCell(2).setCellValue("Descrição");
        header.createCell(3).setCellValue("Preço");
        header.createCell(4).setCellValue("Quantidade");
        header.createCell(5).setCellValue("Categoria");
        header.createCell(6).setCellValue("Data de Cadastro");
    }

    @Override
    protected void createBody(Sheet sheet) {
        var produtos = repository.findAll();

        int rowIdx = 1;
        for (Produto p : produtos) {
            Row row = sheet.createRow(rowIdx++);
            row.createCell(0).setCellValue(p.getId());
            row.createCell(1).setCellValue(p.getNome());
            row.createCell(2).setCellValue(p.getDescricao());
            row.createCell(3).setCellValue(p.getPreco());
            row.createCell(4).setCellValue(p.getQuantidade());
            row.createCell(5).setCellValue(p.getCategoria().toString());
            row.createCell(6).setCellValue(p.getDataCadastro().toString());
        }
    }

}

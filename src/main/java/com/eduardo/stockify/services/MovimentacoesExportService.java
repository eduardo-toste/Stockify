package com.eduardo.stockify.services;

import com.eduardo.stockify.aws.s3.S3Service;
import com.eduardo.stockify.models.Movimentacao;
import com.eduardo.stockify.repositories.MovimentacaoRepository;
import com.eduardo.stockify.utils.ExcelExporter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MovimentacoesExportService extends ExcelExporter<Movimentacao> {

    private final MovimentacaoRepository repository;

    public MovimentacoesExportService(S3Service s3Service, MovimentacaoRepository movimentacaoRepository) {
        super(s3Service);
        this.repository = movimentacaoRepository;
    }

    @Override
    protected void createHeader(Sheet sheet) {
        Row header = sheet.createRow(0);
        header.createCell(0).setCellValue("ID");
        header.createCell(1).setCellValue("Produto");
        header.createCell(2).setCellValue("Tipo");
        header.createCell(3).setCellValue("Quantidade");
        header.createCell(4).setCellValue("Data");
    }

    @Override
    protected void createBody(Sheet sheet) {
        var movimentacoes = repository.findAll();

        int rowIdx = 1;
        for (Movimentacao m : movimentacoes) {
            Row row = sheet.createRow(rowIdx++);
            row.createCell(0).setCellValue(m.getId());
            row.createCell(1).setCellValue(m.getProduto().getNome());
            row.createCell(2).setCellValue(m.getTipo().toString());
            row.createCell(3).setCellValue(m.getQuantidade());
            row.createCell(4).setCellValue(m.getData().toString());
        }
    }

}

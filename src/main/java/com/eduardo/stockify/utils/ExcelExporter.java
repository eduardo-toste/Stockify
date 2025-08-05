package com.eduardo.stockify.utils;

import com.eduardo.stockify.aws.s3.S3Service;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.http.ResponseEntity;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.Duration;
import java.util.Map;
import java.util.UUID;

@RequiredArgsConstructor
public abstract class ExcelExporter<T> {

    private final S3Service s3Service;

    public Map<String, String> enviarParaS3(String sheetName) throws IOException {
        byte[] excelBytes = export(sheetName);

        String fileName = "exportacoes/" + sheetName.toLowerCase() + "-" + UUID.randomUUID() + ".xlsx";

        s3Service.uploadFile(
                excelBytes,
                fileName,
                "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"
        );

        String presignedUrl = s3Service.generatePresignerUrl(fileName, Duration.ofMinutes(30));

        return Map.of("url", presignedUrl);
    }

    private byte[] export(String sheetName) throws IOException {
        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Sheet sheet = workbook.createSheet(sheetName);
            createHeader(sheet);
            createBody(sheet);
            workbook.write(out);
            return out.toByteArray();
        }
    }

    protected abstract void createHeader(Sheet sheet);

    protected abstract void createBody(Sheet sheet);

}

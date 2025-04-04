package com.eduardo.stockify.utils;

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public abstract class ExcelExporter<T> {

    public byte[] export(String sheetName) throws IOException {
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

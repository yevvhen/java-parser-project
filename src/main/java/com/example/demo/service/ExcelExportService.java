package com.example.demo.service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.sl.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.*;
import com.example.demo.entity.Product;

@Service
public class ExcelExportService {
    public byte[] generateExcel(List<Product> products) throws IOException {
        try (XSSFWorkbook workbook = new XSSFWorkbook(); ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            XSSFSheet sheet = workbook.createSheet("Products");

            XSSFRow headerRow = sheet.createRow(0);
            String[] headers = {"Name", "URL", "Price", "Details"};

            for (int i = 0; i < headers.length; i++) {
                headerRow.createCell(i).setCellValue(headers[i]);
            }

           
            int rowNum = 1;
            for (Product product : products) {
                Row row = sheet.createRow(rowNum++);

                row.createCell(0).setCellValue(product.getName() != null ? product.getName() : "N/A");
                row.createCell(1).setCellValue(product.getUrl() != null ? product.getUrl() : "N/A");
                row.createCell(2).setCellValue(product.getPrice() != null ? product.getPrice() : "N/A");

                String details = (product.getDetails() != null && !product.getDetails().isEmpty())
                        ? product.getDetails().entrySet().stream()
                        .map(entry -> entry.getKey() + ": " + entry.getValue())
                        .collect(Collectors.joining(", "))
                        : "No details";

                row.createCell(3).setCellValue(details);
            }

            workbook.write(outputStream);
            return outputStream.toByteArray();
        }
    }
}

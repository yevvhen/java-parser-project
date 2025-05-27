package com.example.demo.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.entity.Product;
import com.example.demo.service.ExcelExportService;
import com.example.demo.service.ProductScraperService;

@RestController
@RequestMapping("/products")
public class ProductController {
    private final ProductScraperService scraperService;
    private final ExcelExportService excelService;

    public ProductController(ProductScraperService scraperService, ExcelExportService excelService) {
        this.scraperService = scraperService;
        this.excelService = excelService;
    }

    @GetMapping("/scrape")
    public List<Product> getScrapedProducts(@RequestParam String url) {
        try {
            return scraperService.scrapeProducts(url);
        } catch (IOException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
    
    @GetMapping("/scrape/xlsx")
    public ResponseEntity<byte[]> getScrapedProductsAsExcel(@RequestParam String url) {
        try {
            List<Product> products = scraperService.scrapeProducts(url);
            byte[] excelData = excelService.generateExcel(products);
            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Disposition", "attachment; filename=products.xlsx");
            return new ResponseEntity<>(excelData, headers, HttpStatus.OK);
        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}

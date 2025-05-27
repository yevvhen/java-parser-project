package com.example.demo.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.entity.Product;
import com.example.demo.service.ProductScraperService;

@Controller
public class ProductViewController {
    private final ProductScraperService scraperService;

    public ProductViewController(ProductScraperService scraperService) {
        this.scraperService = scraperService;
    }

    @GetMapping("/view-products")
    public String getProducts(@RequestParam(value = "url", required = false, defaultValue = "https://example.com/products") String url, Model model) {
        try {
            List<Product> products = scraperService.scrapeProducts(url);
            model.addAttribute("products", products);
            model.addAttribute("url", url);
        } catch (IOException e) {
            model.addAttribute("error", "Error while extracting!");
        }
        return "products";
    }
}

package com.example.demo.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jsoup.*;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.entity.Product;

@Service
public class ProductScraperService {
	
	@Autowired
	private CurrencyService currencyService;
	
    public List<Product> scrapeProducts(String baseUrl) throws IOException {
        List<Product> products = new ArrayList<>();
        Document doc = Jsoup.connect(baseUrl).get();
        
        System.out.print(doc);
        
        Elements items = doc.select(".prdl-item__info");

        for (Element item : items) {
            Element linkElement = item.selectFirst("a");
            Element priceElement = item.selectFirst(".products-list-item-price__actions-price-current");
            
            if (linkElement != null && priceElement != null) {
                String name = linkElement.text();
                String url = linkElement.absUrl("href");
                String price = String.valueOf(currencyService.getCurrency(Float.parseFloat(priceElement.text().replaceAll("[^0-9,.]", "").replace(",", "."))));
                Map<String, String> details = scrapeProductDetails(url);
                
                products.add(new Product(name, url, price, details));
            }
        }
        return products;
    }

    private Map<String, String> scrapeProductDetails(String productUrl) throws IOException {
        Map<String, String> details = new HashMap<>();
        Document productDoc = Jsoup.connect(productUrl).get();
        Elements attributes = productDoc.select(".ftr-item__name");
        Elements values = productDoc.select(".ftr-item__value");

        for (int i = 0; i < Math.min(attributes.size(), values.size()); i++) {
            details.put(attributes.get(i).text(), values.get(i).text());
        }
        return details;
    }
}

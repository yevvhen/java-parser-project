package com.example.demo.entity;

import java.util.Map;

import jakarta.persistence.*;


public class Product {
    private String name;
    private String url;
    private String price;
    private Map<String, String> details;

    public Product(String name, String url, String price, Map<String, String> details) {
        this.name = name;
        this.url = url;
        this.price = price;
        this.details = details;
    }

    public String getName() { return name; }
    public String getUrl() { return url; }
    public String getPrice() { return price; }
    public Map<String, String> getDetails() { return details; }
}

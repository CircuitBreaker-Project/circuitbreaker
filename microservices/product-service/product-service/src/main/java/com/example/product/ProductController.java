package com.example.product;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProductController {

    @GetMapping("/api/products")
    public String getProducts() {
        return "Product Service is working!";
    }

    @GetMapping("/api/products/1")
    public String getProduct() {
        return "Product 1: Laptop";
    }
}
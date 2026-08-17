package com.circuitbreaker.productservice.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProductController {

    @RequestMapping("/products")
    public String products() {
        return "Product Service Working";
    }
}

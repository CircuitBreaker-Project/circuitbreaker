package com.example.demo;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class InventoryController {

    @GetMapping("/api/inventory")
    public String inventory() {
        return "Inventory Service is working!";
    }
}
package com.circuitbreaker.gateway.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
public class FallbackController {

    @GetMapping("/fallback/product")
    public ResponseEntity<Map<String, String>> productFallback() {
        return ResponseEntity.ok(
                Map.of(
                        "service", "product-service",
                        "status", "unavailable",
                        "message", "Product service is temporarily unavailable. Please try again later."
                )
        );
    }

    @GetMapping("/fallback/inventory")
    public ResponseEntity<Map<String, String>> inventoryFallback() {
        return ResponseEntity.ok(
                Map.of(
                        "service", "inventory-service",
                        "status", "unavailable",
                        "message", "Inventory service is temporarily unavailable. Please try again later."
                )
        );
    }

    @GetMapping("/fallback/recommendation")
    public ResponseEntity<Map<String, Object>> recommendationFallback() {

        Map<String, Object> response = new LinkedHashMap<>();

        response.put("service", "recommendation-service");
        response.put("status", "fallback");
        response.put(
                "message",
                "Recommendation service is temporarily unavailable. Showing top sellers instead."
        );

        response.put(
                "recommendations",
                List.of(
                        "Laptop",
                        "Wireless Headphones",
                        "Smartphone",
                        "Mechanical Keyboard"
                )
        );

        return ResponseEntity.ok(response);
    }
}
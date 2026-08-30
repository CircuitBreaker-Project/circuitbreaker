package com.circuitbreaker.gateway;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.Map;

@RestController
public class FallbackController {

    @GetMapping("/fallback/products")
    public Mono<Map<String, Object>> productFallback() {
        return Mono.just(Map.of(
                "service", "product-service",
                "status", "fallback",
                "message", "Product Service is temporarily unavailable",
                "statusCode", HttpStatus.SERVICE_UNAVAILABLE.value()
        ));
    }

    @GetMapping("/fallback/inventory")
    public Mono<Map<String, Object>> inventoryFallback() {
        return Mono.just(Map.of(
                "service", "inventory-service",
                "status", "fallback",
                "message", "Inventory Service is temporarily unavailable",
                "statusCode", HttpStatus.SERVICE_UNAVAILABLE.value()
        ));
    }

    @GetMapping("/fallback/recommendations")
    public Mono<Map<String, Object>> recommendationFallback() {
        return Mono.just(Map.of(
                "service", "recommendation-service",
                "status", "fallback",
                "message", "Recommendation Service is temporarily unavailable",
                "statusCode", HttpStatus.SERVICE_UNAVAILABLE.value()
        ));
    }
}
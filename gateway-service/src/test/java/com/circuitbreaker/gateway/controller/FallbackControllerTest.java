package com.circuitbreaker.gateway.controller;

import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class FallbackControllerTest {

    private final FallbackController fallbackController =
            new FallbackController();

    @Test
    void recommendationFallbackShouldReturnTopSellerData() {

        ResponseEntity<Map<String, Object>> response =
                fallbackController.recommendationFallback();

        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());

        Map<String, Object> body = response.getBody();

        assertEquals("recommendation-service", body.get("service"));
        assertEquals("fallback", body.get("status"));

        assertNotNull(body.get("message"));
        assertTrue(
                body.get("message").toString().toLowerCase().contains("top sellers")
        );

        assertTrue(body.get("recommendations") instanceof List);

        @SuppressWarnings("unchecked")
        List<String> recommendations =
                (List<String>) body.get("recommendations");

        assertEquals(4, recommendations.size());

        assertTrue(recommendations.contains("Laptop"));
        assertTrue(recommendations.contains("Wireless Headphones"));
        assertTrue(recommendations.contains("Smartphone"));
        assertTrue(recommendations.contains("Mechanical Keyboard"));
    }

    @Test
    void productFallbackShouldReturnUnavailableStatus() {

        ResponseEntity<Map<String, String>> response =
                fallbackController.productFallback();

        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());

        assertEquals(
                "product-service",
                response.getBody().get("service")
        );

        assertEquals(
                "unavailable",
                response.getBody().get("status")
        );
    }

    @Test
    void inventoryFallbackShouldReturnUnavailableStatus() {

        ResponseEntity<Map<String, String>> response =
                fallbackController.inventoryFallback();

        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());

        assertEquals(
                "inventory-service",
                response.getBody().get("service")
        );

        assertEquals(
                "unavailable",
                response.getBody().get("status")
        );
    }
}
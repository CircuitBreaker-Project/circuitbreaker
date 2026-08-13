package com.circuitbreaker.microservice.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class Microservice1Controller {

    @GetMapping("/service1/hello")
    public Map<String, String> hello() {
        Map<String, String> response = new HashMap<>();
        response.put("service", "microservice-1");
        response.put("message", "Hello from Microservice 1");
        response.put("status", "active");
        return response;
    }

    @GetMapping("/service1/health")
    public Map<String, String> health() {
        Map<String, String> response = new HashMap<>();
        response.put("service", "microservice-1");
        response.put("status", "healthy");
        return response;
    }
}

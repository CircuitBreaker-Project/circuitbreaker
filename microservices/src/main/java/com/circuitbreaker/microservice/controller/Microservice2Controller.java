package com.circuitbreaker.microservice.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class Microservice2Controller {

    @GetMapping("/service2/hello")
    public Map<String, String> hello() {
        Map<String, String> response = new HashMap<>();
        response.put("service", "microservice-2");
        response.put("message", "Hello from Microservice 2");
        response.put("status", "active");
        return response;
    }

    @GetMapping("/service2/health")
    public Map<String, String> health() {
        Map<String, String> response = new HashMap<>();
        response.put("service", "microservice-2");
        response.put("status", "healthy");
        return response;
    }
}

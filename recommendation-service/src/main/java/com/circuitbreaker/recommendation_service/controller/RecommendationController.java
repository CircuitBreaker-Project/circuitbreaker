package com.circuitbreaker.recommendationservice.controller;

import com.circuitbreaker.recommendationservice.model.Recommendation;
import com.circuitbreaker.recommendationservice.service.RecommendationService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/recommendations")
public class RecommendationController {

    private final RecommendationService service;

    public RecommendationController(RecommendationService service) {
        this.service = service;
    }

    @GetMapping
    public List<Recommendation> getAll() {
        return service.getAll();
    }

    @GetMapping("/{productId}")
    public Recommendation getById(@PathVariable Long productId) {
        return service.getById(productId);
    }

    @GetMapping("/slow")
    public String slow() throws InterruptedException {
        Thread.sleep(5000);
        return "Slow recommendation response";
    }

    @GetMapping("/fail")
    public String fail() {
        throw new RuntimeException("Recommendation service failure");
    }
}

package com.circuitbreaker.recommendationservice.service;

import com.circuitbreaker.recommendationservice.model.Recommendation;
import com.circuitbreaker.recommendationservice.repository.RecommendationRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RecommendationService {

    private final RecommendationRepository repository;

    public RecommendationService(RecommendationRepository repository) {
        this.repository = repository;
    }

    public List<Recommendation> getAll() {
        return repository.findAll();
    }

    public Recommendation getById(Long productId) {
        return repository.findById(productId).orElse(null);
    }
}

package com.circuitbreaker.recommendationservice.repository;

import com.circuitbreaker.recommendationservice.model.Recommendation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecommendationRepository
        extends JpaRepository<Recommendation, Long> {
}
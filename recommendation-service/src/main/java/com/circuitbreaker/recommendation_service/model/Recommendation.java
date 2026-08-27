package com.circuitbreaker.recommendationservice.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Recommendation {

    @Id
    private Long productId;

    private String recommendation;

    public Recommendation() {
    }

    public Recommendation(Long productId, String recommendation) {
        this.productId = productId;
        this.recommendation = recommendation;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public String getRecommendation() {
        return recommendation;
    }

    public void setRecommendation(String recommendation) {
        this.recommendation = recommendation;
    }
}
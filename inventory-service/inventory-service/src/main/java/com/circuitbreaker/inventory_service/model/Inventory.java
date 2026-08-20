package com.circuitbreaker.inventory_service.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Inventory {

    @Id
    private Long productId;

    private Integer quantity;

    public Inventory() {
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}

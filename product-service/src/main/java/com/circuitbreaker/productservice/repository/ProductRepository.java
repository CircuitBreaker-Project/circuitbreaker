package com.circuitbreaker.productservice.repository;

import com.circuitbreaker.productservice.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}

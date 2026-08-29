package com.circuitbreaker.inventory_service.repository;

import com.circuitbreaker.inventory_service.model.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InventoryRepository extends JpaRepository<Inventory, Long> {
}

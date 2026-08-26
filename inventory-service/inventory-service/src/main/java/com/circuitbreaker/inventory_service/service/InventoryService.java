package com.circuitbreaker.inventory_service.service;

import com.circuitbreaker.inventory_service.model.Inventory;
import com.circuitbreaker.inventory_service.repository.InventoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InventoryService {

    @Autowired
    private InventoryRepository repository;

    public List<Inventory> getAllInventory() {
        return repository.findAll();
    }

    public Inventory saveInventory(Inventory inventory) {
        return repository.save(inventory);
    }

    // GET by productId
    public Inventory getInventory(Long productId) {
        return repository.findById(productId).orElse(null);
    }

    // UPDATE
    public Inventory updateInventory(Long productId, Inventory inventory) {
        inventory.setProductId(productId);
        return repository.save(inventory);
    }

    // DELETE
    public void deleteInventory(Long productId) {
        repository.deleteById(productId);
    }
}
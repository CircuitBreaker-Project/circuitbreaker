package com.circuitbreaker.inventory_service.controller;

import com.circuitbreaker.inventory_service.model.Inventory;
import com.circuitbreaker.inventory_service.service.InventoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/inventory")
public class InventoryController {

    @Autowired
    private InventoryService service;

    // GET all inventory
    @GetMapping
    public List<Inventory> getAllInventory() {
        return service.getAllInventory();
    }

    // POST new inventory
    @PostMapping
    public Inventory addInventory(@RequestBody Inventory inventory) {
        return service.saveInventory(inventory);
    }

    // GET inventory by productId
    @GetMapping("/{productId}")
    public Inventory getInventory(@PathVariable Long productId) {
        return service.getInventory(productId);
    }

    // UPDATE inventory
    @PutMapping("/{productId}")
    public Inventory updateInventory(@PathVariable Long productId,
                                     @RequestBody Inventory inventory) {
        return service.updateInventory(productId, inventory);
    }

    // DELETE inventory
    @DeleteMapping("/{productId}")
    public void deleteInventory(@PathVariable Long productId) {
        service.deleteInventory(productId);
    }
}
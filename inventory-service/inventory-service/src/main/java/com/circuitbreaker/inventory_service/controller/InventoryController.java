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

    @GetMapping
    public List<Inventory> getAllInventory() {
        return service.getAllInventory();
    }

    @PostMapping
    public Inventory addInventory(@RequestBody Inventory inventory) {
        return service.saveInventory(inventory);
    }
}
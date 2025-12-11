package com.shivamsrivastav.distributedpatterns.cqrs.controller;

import com.shivamsrivastav.distributedpatterns.cqrs.model.Product;
import com.shivamsrivastav.distributedpatterns.cqrs.service.ProductCommandService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

/**
 * Controller for Write Operations (Commands).
 *
 * @author Shivam Srivastav
 */
@RestController
@RequestMapping("/api/cqrs/commands/products")
public class ProductCommandController {

    private final ProductCommandService commandService;

    public ProductCommandController(ProductCommandService commandService) {
        this.commandService = commandService;
    }

    @PostMapping
    public ResponseEntity<Product> createProduct(@RequestBody Product product) {
        return ResponseEntity.ok(commandService.createProduct(product));
    }

    @PatchMapping("/{id}/price")
    public ResponseEntity<Void> updatePrice(@PathVariable Long id, @RequestParam BigDecimal price) {
        commandService.updatePrice(id, price);
        return ResponseEntity.ok().build();
    }
}

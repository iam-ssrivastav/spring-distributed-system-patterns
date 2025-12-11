package com.shivamsrivastav.distributedpatterns.cqrs.controller;

import com.shivamsrivastav.distributedpatterns.cqrs.model.Product;
import com.shivamsrivastav.distributedpatterns.cqrs.service.ProductQueryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller for Read Operations (Queries).
 *
 * @author Shivam Srivastav
 */
@RestController
@RequestMapping("/api/cqrs/queries/products")
public class ProductQueryController {

    private final ProductQueryService queryService;

    public ProductQueryController(ProductQueryService queryService) {
        this.queryService = queryService;
    }

    @GetMapping
    public ResponseEntity<List<Product>> getAllProducts() {
        return ResponseEntity.ok(queryService.getAllProducts());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> getProduct(@PathVariable Long id) {
        return ResponseEntity.ok(queryService.getProductById(id));
    }
}

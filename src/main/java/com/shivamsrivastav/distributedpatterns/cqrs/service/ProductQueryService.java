package com.shivamsrivastav.distributedpatterns.cqrs.service;

import com.shivamsrivastav.distributedpatterns.cqrs.model.Product;
import com.shivamsrivastav.distributedpatterns.cqrs.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Query Service for CQRS.
 * <p>
 * Responsible ONLY for "Read" operations.
 * Optimized for fetching data. In a real system, this might query a
 * Read-Replica or caching layer.
 * </p>
 *
 * @author Shivam Srivastav
 */
@Service
@Transactional(readOnly = true)
public class ProductQueryService {

    private final ProductRepository productRepository;

    public ProductQueryService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Product getProductById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));
    }
}

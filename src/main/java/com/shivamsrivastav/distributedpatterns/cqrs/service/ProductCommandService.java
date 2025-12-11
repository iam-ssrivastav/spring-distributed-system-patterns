package com.shivamsrivastav.distributedpatterns.cqrs.service;

import com.shivamsrivastav.distributedpatterns.cqrs.model.Product;
import com.shivamsrivastav.distributedpatterns.cqrs.repository.ProductRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Command Service for CQRS.
 * <p>
 * Responsible ONLY for "Write" operations (Create, Update, Delete).
 * In a full event-sourced system, this would publish an event after saving.
 * </p>
 *
 * @author Shivam Srivastav
 */
@Service
public class ProductCommandService {

    private static final Logger log = LoggerFactory.getLogger(ProductCommandService.class);

    private final ProductRepository productRepository;

    public ProductCommandService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    /**
     * Handle Create Product Command.
     *
     * @param product The product state to create
     * @return The created product
     */
    @Transactional
    public Product createProduct(Product product) {
        log.info("Handling Create Product Command for: {}", product.getName());
        return productRepository.save(product);
    }

    /**
     * Handle Update Price Command.
     *
     * @param id       Product ID
     * @param newPrice New price
     */
    @Transactional
    public void updatePrice(Long id, java.math.BigDecimal newPrice) {
        log.info("Handling Update Price Command for ID: {}", id);
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        product.setPrice(newPrice);
        productRepository.save(product);
    }
}

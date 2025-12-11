package com.shivamsrivastav.distributedpatterns.saga.orchestration.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * Mock Inventory Service to demonstrate Saga steps.
 * <p>
 * In a real microservices architecture, this would be a remote service called
 * via REST/gRPC.
 * Use simple logic here: if productId is "OUT_OF_STOCK", throw exception.
 * </p>
 *
 * @author Shivam Srivastav
 */
@Service
public class InventoryService {

    private static final Logger log = LoggerFactory.getLogger(InventoryService.class);

    /**
     * Reserves inventory for a product.
     *
     * @param productId The product ID
     * @param quantity  The quantity to reserve
     * @return true if successful
     * @throws RuntimeException if product is out of stock (simulated)
     */
    public boolean reserveInventory(String productId, Integer quantity) {
        log.info("Attempting to reserve inventory for Product: {}, Quantity: {}", productId, quantity);

        // Simulation of a business rule or failure
        if ("OUT_OF_STOCK".equalsIgnoreCase(productId)) {
            log.error("Inventory reservation failed: Product {} is out of stock", productId);
            throw new RuntimeException("Product is out of stock");
        }

        log.info("Inventory reserved successfully for Product: {}", productId);
        return true;
    }

    /**
     * Compensating Transaction: Releases previously reserved inventory.
     *
     * @param productId The product ID
     * @param quantity  The quantity to release
     */
    public void releaseInventory(String productId, Integer quantity) {
        log.info("Compensating Transaction: Releasing inventory for Product: {}, Quantity: {}", productId, quantity);
        // Logic to restore inventory count would go here
    }
}

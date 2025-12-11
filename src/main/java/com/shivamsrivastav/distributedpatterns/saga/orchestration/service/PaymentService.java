package com.shivamsrivastav.distributedpatterns.saga.orchestration.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 * Mock Payment Service to demonstrate Saga phases.
 * <p>
 * Simulates payment processing. Fails if the price is substantially high (e.g.,
 * > 1000)
 * to demonstrate compensation triggering in the Orchestrator.
 * </p>
 *
 * @author Shivam Srivastav
 */
@Service
public class PaymentService {

    private static final Logger log = LoggerFactory.getLogger(PaymentService.class);

    /**
     * Processes payment for an order.
     *
     * @param customerId The customer ID
     * @param amount     The total amount to charge
     * @return true if successful
     * @throws RuntimeException if payment is rejected (simulated)
     */
    public boolean processPayment(String customerId, BigDecimal amount) {
        log.info("Attempting to process payment for Customer: {}, Amount: {}", customerId, amount);

        // Simulation: Fails if amount is greater than 1000 (e.g., insufficient funds)
        if (amount.compareTo(new BigDecimal("1000")) > 0) {
            log.error("Payment processing failed: Amount {} exceeds limit for Customer {}", amount, customerId);
            throw new RuntimeException("Insufficient funds or limit exceeded");
        }

        log.info("Payment processed successfully for Customer: {}", customerId);
        return true;
    }

    /**
     * Compensating Transaction: Refunds a previously processed payment.
     *
     * @param customerId The customer ID
     * @param amount     The amount to refund
     */
    public void refundPayment(String customerId, BigDecimal amount) {
        log.info("Compensating Transaction: Refunding payment for Customer: {}, Amount: {}", customerId, amount);
        // Logic to process refund would go here
    }
}

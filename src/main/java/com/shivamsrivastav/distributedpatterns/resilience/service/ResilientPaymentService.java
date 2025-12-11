package com.shivamsrivastav.distributedpatterns.resilience.service;

import io.github.resilience4j.bulkhead.annotation.Bulkhead;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Random;

/**
 * Service demonstrating Resilience4j patterns.
 * <p>
 * This simulates an unstable external service call.
 * Check application.properties for configuration thresholds.
 * </p>
 *
 * @author Shivam Srivastav
 */
@Service
public class ResilientPaymentService {

    private static final Logger log = LoggerFactory.getLogger(ResilientPaymentService.class);
    private static final String SERVICE_NAME = "paymentService";

    /**
     * Simulates a risky external call that might fail randomly.
     * Protected by Circuit Breaker, Retry, and Bulkhead.
     *
     * @return Success message
     */
    @CircuitBreaker(name = SERVICE_NAME, fallbackMethod = "fallbackPayment")
    @Retry(name = SERVICE_NAME)
    @Bulkhead(name = SERVICE_NAME)
    public String processRiskyPayment() {
        log.info("Attempting risky payment processing...");

        // Simulate Random Failure
        if (new Random().nextInt(10) < 6) { // 60% failure chance
            log.error("Payment Service failed!");
            throw new RuntimeException("External Payment Service Unavailable");
        }

        return "Payment Processed Successfully";
    }

    /**
     * Fallback method called when the circuit is open or retries are exhausted.
     *
     * @param t The exception that caused the failure
     * @return Fallback response
     */
    public String fallbackPayment(Throwable t) {
        log.warn("Fallback triggered: {}", t.getMessage());
        return "Fallback: Payment Service is currently unavailable. Please try again later.";
    }
}

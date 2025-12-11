package com.shivamsrivastav.distributedpatterns;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main entry point for the Distributed Patterns Demo application.
 * <p>
 * This application demonstrates various advanced microservices patterns:
 * <ul>
 * <li>Saga (Orchestration & Choreography)</li>
 * <li>CQRS (Command Query Responsibility Segregation)</li>
 * <li>Transactional Outbox</li>
 * <li>Resilience Patterns (Circuit Breaker, Retry, Bulkhead)</li>
 * </ul>
 * 
 * @author Shivam Srivastav
 */
@SpringBootApplication
public class DistributedPatternsApplication {

    public static void main(String[] args) {
        SpringApplication.run(DistributedPatternsApplication.class, args);
    }

}

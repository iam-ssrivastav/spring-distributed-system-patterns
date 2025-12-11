package com.shivamsrivastav.distributedpatterns.resilience.controller;

import com.shivamsrivastav.distributedpatterns.resilience.service.ResilientPaymentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller to test Resilience4j patterns.
 *
 * @author Shivam Srivastav
 */
@RestController
@RequestMapping("/api/resilience")
public class ResilienceController {

    private final ResilientPaymentService resilientPaymentService;

    public ResilienceController(ResilientPaymentService resilientPaymentService) {
        this.resilientPaymentService = resilientPaymentService;
    }

    @GetMapping("/payment")
    public ResponseEntity<String> testResilience() {
        return ResponseEntity.ok(resilientPaymentService.processRiskyPayment());
    }
}

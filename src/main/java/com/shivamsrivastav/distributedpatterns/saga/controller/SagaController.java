package com.shivamsrivastav.distributedpatterns.saga.controller;

import com.shivamsrivastav.distributedpatterns.outbox.model.OutboxEvent;
import com.shivamsrivastav.distributedpatterns.outbox.repository.OutboxRepository;
import com.shivamsrivastav.distributedpatterns.saga.orchestration.model.SagaOrder;
import com.shivamsrivastav.distributedpatterns.saga.orchestration.model.SagaOrderRepository;
import com.shivamsrivastav.distributedpatterns.saga.orchestration.service.OrderSagaOrchestrator;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller to trigger the Saga orchestration flow and view data.
 *
 * @author Shivam Srivastav
 */
@RestController
@RequestMapping("/api/saga")
public class SagaController {

    private final OrderSagaOrchestrator sagaOrchestrator;
    private final SagaOrderRepository sagaOrderRepository;
    private final OutboxRepository outboxRepository;

    public SagaController(OrderSagaOrchestrator sagaOrchestrator, SagaOrderRepository sagaOrderRepository,
            OutboxRepository outboxRepository) {
        this.sagaOrchestrator = sagaOrchestrator;
        this.sagaOrderRepository = sagaOrderRepository;
        this.outboxRepository = outboxRepository;
    }

    /**
     * Creates an order using the Saga Orchestration pattern.
     *
     * @param order The order details
     * @return The processed order with final status
     */
    @PostMapping("/orders")
    public ResponseEntity<SagaOrder> createOrder(@RequestBody SagaOrder order) {
        SagaOrder result = sagaOrchestrator.createOrder(order);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/orders")
    public ResponseEntity<List<SagaOrder>> getAllOrders() {
        return ResponseEntity.ok(sagaOrderRepository.findAll());
    }

    @GetMapping("/outbox")
    public ResponseEntity<List<OutboxEvent>> getAllOutboxEvents() {
        return ResponseEntity.ok(outboxRepository.findAll());
    }
}

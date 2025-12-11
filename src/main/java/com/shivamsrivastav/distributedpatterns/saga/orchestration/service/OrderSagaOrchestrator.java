package com.shivamsrivastav.distributedpatterns.saga.orchestration.service;

import com.shivamsrivastav.distributedpatterns.outbox.service.OutboxService;
import com.shivamsrivastav.distributedpatterns.saga.orchestration.model.OrderStatus;
import com.shivamsrivastav.distributedpatterns.saga.orchestration.model.SagaOrder;
import com.shivamsrivastav.distributedpatterns.saga.orchestration.model.SagaOrderRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Saga Orchestrator Service.
 * <p>
 * This class is the central coordinator (The "Brain") of the Saga.
 * It manages the lifecycle of the transaction:
 * <ol>
 * <li>Create concept of Order (PENDING)</li>
 * <li>Call Inventory Service</li>
 * <li>Call Payment Service</li>
 * <li>Complete Order</li>
 * </ol>
 * If any step fails, it strictly executes "Compensating Transactions" in
 * reverse order
 * to return the system to a consistent state.
 * </p>
 *
 * @author Shivam Srivastav
 */
@Service
public class OrderSagaOrchestrator {

    private static final Logger log = LoggerFactory.getLogger(OrderSagaOrchestrator.class);

    private final SagaOrderRepository orderRepository;
    private final InventoryService inventoryService;
    private final PaymentService paymentService;
    private final OutboxService outboxService;

    public OrderSagaOrchestrator(SagaOrderRepository orderRepository, InventoryService inventoryService,
            PaymentService paymentService, OutboxService outboxService) {
        this.orderRepository = orderRepository;
        this.inventoryService = inventoryService;
        this.paymentService = paymentService;
        this.outboxService = outboxService;
    }

    /**
     * Entry point to start the Create Order Saga.
     *
     * @param order The order request
     * @return The updated order with final status
     */
    @Transactional
    public SagaOrder createOrder(SagaOrder order) {
        // Step 1: Initialize Order
        order.setStatus(OrderStatus.CREATED);
        SagaOrder savedOrder = orderRepository.save(order);
        log.info("Saga started: Order {} created with status CREATED", savedOrder.getId());

        try {
            // Step 2: Reserve Inventory
            inventoryService.reserveInventory(savedOrder.getProductId(), savedOrder.getQuantity());
            savedOrder.setStatus(OrderStatus.INVENTORY_RESERVED);
            orderRepository.save(savedOrder);
            log.info("Order {}: Inventory reserved.", savedOrder.getId());

            // Step 3: Process Payment
            paymentService.processPayment(savedOrder.getCustomerId(), savedOrder.getPrice());
            savedOrder.setStatus(OrderStatus.PAYMENT_PROCESSED);
            orderRepository.save(savedOrder);
            log.info("Order {}: Payment processed.", savedOrder.getId());

            // Step 4: Complete Saga
            savedOrder.setStatus(OrderStatus.COMPLETED);
            orderRepository.save(savedOrder);
            log.info("Saga completed successfully for Order {}", savedOrder.getId());

            // --- OUTBOX PATTERN INTEGRATION ---
            // Save an event to the outbox table in the SAME transaction
            outboxService.saveEvent("ORDER", savedOrder.getId().toString(), "ORDER_COMPLETED", savedOrder);

            return savedOrder;

        } catch (Exception e) {
            log.error("Saga failed for Order {}. Initiating Compensation. Reason: {}", savedOrder.getId(),
                    e.getMessage());
            // Trigger Compensation Logic
            return handleSagaFailure(savedOrder, e.getMessage());
        }
    }

    /**
     * Handles failures by executing compensating transactions based on the current
     * state.
     *
     * @param order         The order that failed
     * @param failureReason Error message
     * @return The order with CANCELLED status
     */
    private SagaOrder handleSagaFailure(SagaOrder order, String failureReason) {
        log.warn("Executing compensating transactions for Order {} currently in state {}", order.getId(),
                order.getStatus());

        // Reverse order of operations based on current status
        switch (order.getStatus()) {
            case PAYMENT_PROCESSED:
                paymentService.refundPayment(order.getCustomerId(), order.getPrice());
                // Fallthrough to reverse inventory as well
            case INVENTORY_RESERVED:
                inventoryService.releaseInventory(order.getProductId(), order.getQuantity());
                break;
            default:
                // If failed at CREATED, no external calls need reversing
                break;
        }

        // Finalize state as FAILED/CANCELLED
        order.setStatus(OrderStatus.CANCELLED);
        order.setFailureReason(failureReason);
        SagaOrder failedOrder = orderRepository.save(order);

        // --- OUTBOX PATTERN INTEGRATION ---
        // Save failure event
        outboxService.saveEvent("ORDER", failedOrder.getId().toString(), "ORDER_CANCELLED", failedOrder);

        log.info("Compensation completed. Order {} marked as CANCELLED.", failedOrder.getId());
        return failedOrder;
    }
}

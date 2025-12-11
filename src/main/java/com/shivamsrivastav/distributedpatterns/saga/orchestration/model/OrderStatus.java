package com.shivamsrivastav.distributedpatterns.saga.orchestration.model;

/**
 * Enum representing the various states of an Order in the Saga lifecycle.
 *
 * @author Shivam Srivastav
 */
public enum OrderStatus {
    CREATED,
    INVENTORY_RESERVED,
    PAYMENT_PROCESSED,
    COMPLETED,
    CANCELLED,
    PAYMENT_FAILED,
    INVENTORY_FAILED
}

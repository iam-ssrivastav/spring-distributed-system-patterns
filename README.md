# Distributed Systems Patterns Demo

This project demonstrates advanced Microservices patterns using Spring Boot. It serves as a reference implementation for handling distributed transactions, scaling reads vs writes, ensuring atomic event publishing, and building resilient systems.

## Patterns Implemented

### 1. Saga Pattern (Orchestration)
**Location:** `com.shivamsrivastav.distributedpatterns.saga`

*   **Problem:** How to maintain data consistency across multiple microservices (e.g., Order, Inventory, Payment) without 2PC?
*   **Solution:** We use an **Orchestrator** (`OrderSagaOrchestrator`) that manages the transaction steps.
*   **Key Components:**
    *   `OrderSagaOrchestrator`: The central coordinator.
    *   `InventoryService` / `PaymentService`: Mock services that can be instructed to "compensate" (undo actions).
    *   **Compensation Logic:** If `PaymentService` fails, the orchestrator triggers `inventoryService.releaseInventory()` to rollback.

### 2. CQRS (Command Query Responsibility Segregation)
**Location:** `com.shivamsrivastav.distributedpatterns.cqrs`

*   **Problem:** Complex domain logic for writes often slows down simple reads.
*   **Solution:** Split the application into two parts:
    *   **Command Side (Write):** `ProductCommandService` - Handles creates/updates. Optimized for domain rules and consistency.
    *   **Query Side (Read):** `ProductQueryService` - Handles reads. Optimized for speed and simple DTO projections.

### 3. Transactional Outbox Pattern
**Location:** `com.shivamsrivastav.distributedpatterns.outbox`

*   **Problem:** "Dual Write Problem". How to update the database *and* publish an event (to Kafka/RabbitMQ) atomically? If the DB commit succeeds but publishing fails, data is inconsistent.
*   **Solution:**
    *   Save the event to an `outbox_events` table within the **same `@Transactional`** block as the business logic.
    *   A separate process (`OutboxEventPublisher`) polls the table and publishes the events safely.

### 4. Resilience Patterns (Resilience4j)
**Location:** `com.shivamsrivastav.distributedpatterns.resilience`

*   **Problem:** Network failures or slow downstream services can cascade and crash the entire system.
*   **Solution:**
    *   **Circuit Breaker:** "Opens" the circuit after a threshold of failures (50%), failing fast without waiting.
    *   **Retry:** Automatically retries transient failures before giving up.
    *   **Bulkhead:** Limits the number of concurrent calls to a specific service to prevent it from exhausting all thread resources.

## How to Run

1.  **Build:**
    ```bash
    mvn clean install
    ```
2.  **Run:**
    ```bash
    mvn spring-boot:run
    ```
3.  **Test Endpoints:**

    *   **Saga (Create Order):**
        ```bash
        curl -X POST http://localhost:8082/api/saga/orders \
             -H "Content-Type: application/json" \
             -d '{"customerId":"cust1", "productId":"prod1", "quantity":1, "price":100}'
        ```
    *   **Saga (Trigger Failure & Rollback):**
        *   Payment fails if `price > 1000`.
        *   Inventory fails if `productId = "OUT_OF_STOCK"`.

    *   **CQRS (Create Product):**
        ```bash
        curl -X POST http://localhost:8082/api/cqrs/commands/products \
             -H "Content-Type: application/json" \
             -d '{"name":"Laptop", "price":1200}'
        ```

    *   **Resilience (Test Circuit Breaker):**
        ```bash
        curl http://localhost:8082/api/resilience/payment
        ```

## Requirements
*   Java 17+
*   Maven

## Author
**Shivam Srivastav**

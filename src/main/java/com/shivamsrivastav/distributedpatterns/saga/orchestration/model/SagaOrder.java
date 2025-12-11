package com.shivamsrivastav.distributedpatterns.saga.orchestration.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.Objects;

/**
 * Entity representing an Order in the Saga Orchestration example.
 *
 * @author Shivam Srivastav
 */
@Entity
@Table(name = "saga_orders")
public class SagaOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String customerId;
    private String productId;
    private Integer quantity;
    private BigDecimal price;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    private String failureReason;

    public SagaOrder() {
    }

    public SagaOrder(Long id, String customerId, String productId, Integer quantity, BigDecimal price,
            OrderStatus status, String failureReason) {
        this.id = id;
        this.customerId = customerId;
        this.productId = productId;
        this.quantity = quantity;
        this.price = price;
        this.status = status;
        this.failureReason = failureReason;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public String getFailureReason() {
        return failureReason;
    }

    public void setFailureReason(String failureReason) {
        this.failureReason = failureReason;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        SagaOrder sagaOrder = (SagaOrder) o;
        return Objects.equals(id, sagaOrder.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "SagaOrder{" +
                "id=" + id +
                ", customerId='" + customerId + '\'' +
                ", productId='" + productId + '\'' +
                ", quantity=" + quantity +
                ", price=" + price +
                ", status=" + status +
                ", failureReason='" + failureReason + '\'' +
                '}';
    }
}

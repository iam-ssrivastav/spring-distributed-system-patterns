package com.shivamsrivastav.distributedpatterns.saga.orchestration.model;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository for managing SagaOrder entities.
 *
 * @author Shivam Srivastav
 */
@Repository
public interface SagaOrderRepository extends JpaRepository<SagaOrder, Long> {
}

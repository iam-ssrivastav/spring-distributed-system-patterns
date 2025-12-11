package com.shivamsrivastav.distributedpatterns.outbox.repository;

import com.shivamsrivastav.distributedpatterns.outbox.model.OutboxEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository to manage Outbox Events.
 *
 * @author Shivam Srivastav
 */
@Repository
public interface OutboxRepository extends JpaRepository<OutboxEvent, Long> {

    /**
     * Find all events that have not been processed yet.
     * 
     * @return List of unprocessed events.
     */
    List<OutboxEvent> findByProcessedFalse();
}

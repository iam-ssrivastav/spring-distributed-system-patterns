package com.shivamsrivastav.distributedpatterns.outbox.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shivamsrivastav.distributedpatterns.outbox.model.OutboxEvent;
import com.shivamsrivastav.distributedpatterns.outbox.repository.OutboxRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service to save events into the Outbox table.
 * 
 * @author Shivam Srivastav
 */
@Service
public class OutboxService {

    private final OutboxRepository outboxRepository;
    private final ObjectMapper objectMapper;

    public OutboxService(OutboxRepository outboxRepository, ObjectMapper objectMapper) {
        this.outboxRepository = outboxRepository;
        this.objectMapper = objectMapper;
    }

    /**
     * Saves an event to the outbox.
     * MUST be called within an existing transaction (REQUIRED propagation implied).
     */
    @Transactional(propagation = Propagation.MANDATORY)
    public void saveEvent(String aggregateType, String aggregateId, String eventType, Object payload) {
        try {
            String payloadJson = objectMapper.writeValueAsString(payload);

            OutboxEvent event = new OutboxEvent();
            event.setAggregateType(aggregateType);
            event.setAggregateId(aggregateId);
            event.setEventType(eventType);
            event.setPayload(payloadJson);
            event.setProcessed(false);

            outboxRepository.save(event);
        } catch (Exception e) {
            throw new RuntimeException("Error while saving outbox event", e);
        }
    }
}

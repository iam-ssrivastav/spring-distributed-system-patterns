package com.shivamsrivastav.distributedpatterns.outbox.job;

import com.shivamsrivastav.distributedpatterns.outbox.model.OutboxEvent;
import com.shivamsrivastav.distributedpatterns.outbox.repository.OutboxRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Scheduled job that acts as the "Relay" for the Outbox Pattern.
 * <p>
 * It polls the outbox table for unprocessed events and publishes them
 * (simulated here by logging). In a real app, this would send to
 * Kafka/RabbitMQ.
 * </p>
 *
 * @author Shivam Srivastav
 */
@Component
@EnableScheduling
public class OutboxEventPublisher {

    private static final Logger log = LoggerFactory.getLogger(OutboxEventPublisher.class);

    private final OutboxRepository outboxRepository;
    private final KafkaTemplate<String, String> kafkaTemplate;

    public OutboxEventPublisher(OutboxRepository outboxRepository, KafkaTemplate<String, String> kafkaTemplate) {
        this.outboxRepository = outboxRepository;
        this.kafkaTemplate = kafkaTemplate;
    }

    @Scheduled(fixedDelay = 5000)
    public void publishUnprocessedEvents() {
        List<OutboxEvent> events = outboxRepository.findByProcessedFalse();

        if (!events.isEmpty()) {
            log.info("Found {} unprocessed outbox events. Publishing...", events.size());

            for (OutboxEvent event : events) {
                try {
                    // Publish to Kafka
                    log.info(">>> Publishing Event to Kafka Topic 'orders': [Type: {}, ID: {}]", event.getEventType(),
                            event.getAggregateId());
                    kafkaTemplate.send("orders", event.getAggregateId(), event.getPayload());

                    // Mark as processed
                    event.setProcessed(true);
                    outboxRepository.save(event);

                } catch (Exception e) {
                    log.error("Failed to publish event {}", event.getId(), e);
                }
            }
        }
    }
}

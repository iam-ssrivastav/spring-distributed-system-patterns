package com.shivamsrivastav.distributedpatterns.common.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

/**
 * Service to consume Kafka messages for verification.
 *
 * @author Shivam Srivastav
 */
@Service
public class KafkaConsumerService {

    private static final Logger log = LoggerFactory.getLogger(KafkaConsumerService.class);

    @KafkaListener(topics = "orders", groupId = "saga-group")
    public void consume(String message) {
        log.info("<<< Received Kafka Message: {}", message);
    }
}

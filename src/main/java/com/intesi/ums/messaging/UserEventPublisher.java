package com.intesi.ums.messaging;

import com.intesi.ums.domain.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.UUID;

/**
 * Publishes user domain events to RabbitMQ.
 *
 * Uses a topic exchange so consumers can subscribe to specific event types
 * via routing key patterns (e.g. "user.#" to receive all user events).
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class UserEventPublisher {

    private final RabbitTemplate rabbitTemplate;

    @Value("${app.messaging.exchange}")
    private String exchange;

    @Value("${app.messaging.routing-key.user-created}")
    private String userCreatedRoutingKey;

    /**
     * Publishes a UserCreatedEvent after a successful user creation.
     *
     * The event is published AFTER the transaction commits to avoid sending
     * a message for a transaction that may ultimately be rolled back.
     * This is handled in the service via @TransactionalEventListener.
     */
    public void publishUserCreated(User user) {
        UserCreatedEvent event = UserCreatedEvent.builder()
            .eventId(UUID.randomUUID())
            .userId(user.getId())
            .username(user.getUsername())
            .email(user.getEmail())
            .roles(user.getRoles())
            .occurredAt(Instant.now())
            .build();

        try {
            rabbitTemplate.convertAndSend(exchange, userCreatedRoutingKey, event);
            log.info("Published UserCreatedEvent for userId={} with eventId={}", user.getId(), event.eventId());
        } catch (Exception e) {
            // Log and do not propagate — messaging failure should not roll back the user creation.
            // In a production system this would feed into an outbox/dead-letter mechanism.
            log.error("Failed to publish UserCreatedEvent for userId={}: {}", user.getId(), e.getMessage(), e);
        }
    }
}

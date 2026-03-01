package com.intesi.ums.messaging;

import com.intesi.ums.domain.ApplicationRole;
import lombok.Builder;

import java.time.Instant;
import java.util.Set;
import java.util.UUID;

/**
 * Domain event published to RabbitMQ when a new user is successfully created.
 *
 * Purpose:
 * Downstream services (e.g. notification service, audit service, Keycloak sync adapter)
 * can subscribe to this event to react asynchronously without tight coupling to this service.
 *
 * The event carries only the data needed by consumers (no sensitive fields like CF).
 */
@Builder
public record UserCreatedEvent(

    /** Unique event ID for idempotency on the consumer side */
    UUID eventId,

    /** The newly created user */
    UUID userId,
    String username,
    String email,
    Set<ApplicationRole> roles,

    /** ISO-8601 timestamp of when the user was created */
    Instant occurredAt

) {}

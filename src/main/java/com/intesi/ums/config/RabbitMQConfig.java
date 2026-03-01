package com.intesi.ums.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * RabbitMQ topology configuration.
 *
 * Exchange: ums.events (topic) — allows flexible routing key patterns
 * Queue:    ums.user.created   — receives user creation events
 * DLQ:      ums.user.created.dlq — receives messages that fail processing
 *
 * Routing key: user.created
 *
 * Design rationale for topic exchange over direct/fanout:
 * - Subscribers can use wildcards (e.g. "user.#") to receive all user events
 * - Makes it easy to add new event types (user.updated, user.deleted) without topology changes
 */
@Configuration
public class RabbitMQConfig {

    public static final String EXCHANGE        = "ums.events";
    public static final String QUEUE           = "ums.user.created";
    public static final String DLQ             = "ums.user.created.dlq";
    public static final String DLX             = "ums.events.dlx";
    public static final String ROUTING_KEY     = "user.created";

    // ---- Exchange ----

    @Bean
    TopicExchange umsEventsExchange() {
        return ExchangeBuilder.topicExchange(EXCHANGE).durable(true).build();
    }

    @Bean
    DirectExchange deadLetterExchange() {
        return ExchangeBuilder.directExchange(DLX).durable(true).build();
    }

    // ---- Queues ----

    @Bean
    Queue userCreatedQueue() {
        return QueueBuilder.durable(QUEUE)
            .withArgument("x-dead-letter-exchange", DLX)
            .withArgument("x-dead-letter-routing-key", DLQ)
            .build();
    }

    @Bean
    Queue userCreatedDlq() {
        return QueueBuilder.durable(DLQ).build();
    }

    // ---- Bindings ----

    @Bean
    Binding userCreatedBinding(Queue userCreatedQueue, TopicExchange umsEventsExchange) {
        return BindingBuilder.bind(userCreatedQueue).to(umsEventsExchange).with(ROUTING_KEY);
    }

    @Bean
    Binding dlqBinding(Queue userCreatedDlq, DirectExchange deadLetterExchange) {
        return BindingBuilder.bind(userCreatedDlq).to(deadLetterExchange).with(DLQ);
    }

    // ---- Message Converter ----

    @Bean
    MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(jsonMessageConverter());
        return template;
    }
}

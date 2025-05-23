package com.github.kegszool.notification.messaging.config;

import org.springframework.context.annotation.Bean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import com.github.kegszool.messaging.config.RabbitConfiguration;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;

@Configuration
public class UpdateNotificationMessagingConfig extends RabbitConfiguration {

    private final String requestQueue;
    private final String responseQueue;
    private final String requestRoutingKey;
    private final String responseRoutingKey;

    public UpdateNotificationMessagingConfig(
            @Value("${spring.rabbitmq.queues.update_notification_request}") String requestQueue,
            @Value("${spring.rabbitmq.queues.update_notification_response}") String responseQueue,
            @Value("${spring.rabbitmq.template.routing-key.update_notification_request}") String requestRoutingKey,
            @Value("${spring.rabbitmq.template.routing-key.update_notification_response}") String responseRoutingKey
    ) {
        this.requestQueue = requestQueue;
        this.responseQueue = responseQueue;
        this.requestRoutingKey = requestRoutingKey;
        this.responseRoutingKey = responseRoutingKey;
    }

    @Bean
    public Queue updateNotificationRequestQueue() {
        return new Queue(requestQueue, true, false, false, queueArgs);
    }

    @Bean
    public Queue updateNotificationResponseQueue() {
        return new Queue(responseQueue, true, false, false, queueArgs);
    }

    @Bean
    public Binding bindUpdateNotificationRequest() {
        return BindingBuilder.bind(updateNotificationRequestQueue())
                .to(exchange())
                .with(requestRoutingKey);
    }

    @Bean
    public Binding bindUpdateNotificationResponse() {
        return BindingBuilder.bind(updateNotificationResponseQueue())
                .to(exchange())
                .with(responseRoutingKey);
    }
}
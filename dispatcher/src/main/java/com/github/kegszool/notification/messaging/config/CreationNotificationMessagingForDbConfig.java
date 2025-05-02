package com.github.kegszool.notification.messaging.config;

import org.springframework.context.annotation.Bean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import com.github.kegszool.messaging.config.RabbitConfiguration;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;

@Configuration
public class CreationNotificationMessagingForDbConfig extends RabbitConfiguration {

    private final String requestQueue;
    private final String responseQueue;
    private final String requestRoutingKey;
    private final String responseRoutingKey;

    public CreationNotificationMessagingForDbConfig(
            @Value("${spring.rabbitmq.queues.create_notification_request_for_db}") String requestQueue,
            @Value("${spring.rabbitmq.queues.create_notification_response_from_db}") String responseQueue,
            @Value("${spring.rabbitmq.template.routing-key.create_notification_request_for_db}") String requestRoutingKey,
            @Value("${spring.rabbitmq.template.routing-key.create_notification_response_from_db}") String responseRoutingKey
    ) {
        this.requestQueue = requestQueue;
        this.responseQueue = responseQueue;
        this.requestRoutingKey = requestRoutingKey;
        this.responseRoutingKey = responseRoutingKey;
    }

    @Bean
    public Queue createNotificationRequestQueue() {
        return new Queue(requestQueue, true, false, false, queueArgs);
    }

    @Bean
    public Queue createNotificationResponseQueue() {
        return new Queue(responseQueue, true, false, false, queueArgs);
    }

    @Bean
    public Binding bindCreationNotificationRequest() {
        return BindingBuilder.bind(createNotificationRequestQueue())
                .to(exchange())
                .with(requestRoutingKey);
    }

    @Bean
    public Binding bindCreationNotificationResponse() {
        return BindingBuilder.bind(createNotificationResponseQueue())
                .to(exchange())
                .with(responseRoutingKey);
    }
}

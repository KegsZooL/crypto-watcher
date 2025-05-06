package com.github.kegszool.notification.messaging.config;

import org.springframework.context.annotation.Bean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import com.github.kegszool.messaging.config.RabbitConfiguration;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;

@Configuration
public class NotificationWebSocketSubscriptionMessagingConfig extends RabbitConfiguration {

    private final String queue;
    private final String routingKey;

    public NotificationWebSocketSubscriptionMessagingConfig(
            @Value("${spring.rabbitmq.queues.notification_websocket_subscription}") String queue,
            @Value("${spring.rabbitmq.template.routing-key.notification_websocket_subscription}") String routingKey
    ) {
        this.queue = queue;
        this.routingKey = routingKey;
    }

    @Bean
    public Queue notificationWebSocketSubscriptionQueue() {
        return new Queue(queue, true, false, false, queueArgs);
    }

    @Bean
    public Binding bindNotificationWebSocketSubscription() {
        return BindingBuilder.bind(notificationWebSocketSubscriptionQueue())
                .to(exchange())
                .with(routingKey);
    }
}
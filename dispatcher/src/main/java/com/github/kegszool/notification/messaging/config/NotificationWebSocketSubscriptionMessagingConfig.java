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

    private final String subscribeQueue;
    private final String subscribeRoutingKey;

    private final String unsubscribeQueue;
    private final String unsubscribeRoutingKey;

    public NotificationWebSocketSubscriptionMessagingConfig(
            @Value("${spring.rabbitmq.queues.notification_websocket_subscription}") String subscribeQueue,
            @Value("${spring.rabbitmq.template.routing-key.notification_websocket_subscription}") String subscribeRoutingKey,
            @Value("${spring.rabbitmq.queues.notification_websocket_unsubscription}") String unsubscribeQueue,
            @Value("${spring.rabbitmq.template.routing-key.notification_websocket_unsubscription}") String unsubscribeRoutingKey
    ) {
        this.subscribeQueue = subscribeQueue;
        this.subscribeRoutingKey = subscribeRoutingKey;
        this.unsubscribeQueue = unsubscribeQueue;
        this.unsubscribeRoutingKey = unsubscribeRoutingKey;
    }

    @Bean
    public Queue notificationWebSocketSubscriptionQueue() {
        return new Queue(subscribeQueue, true, false, false, queueArgs);
    }

    @Bean
    public Binding bindNotificationWebSocketSubscription() {
        return BindingBuilder.bind(notificationWebSocketSubscriptionQueue())
                .to(exchange())
                .with(subscribeRoutingKey);
    }

    @Bean
    public Queue notificationWebSocketUnsubscriptionQueue() {
        return new Queue(unsubscribeQueue, true, false, false, queueArgs);
    }

    @Bean
    public Binding bindNotificationWebSocketUnsubscription() {
        return BindingBuilder.bind(notificationWebSocketUnsubscriptionQueue())
                .to(exchange())
                .with(unsubscribeRoutingKey);
    }
}
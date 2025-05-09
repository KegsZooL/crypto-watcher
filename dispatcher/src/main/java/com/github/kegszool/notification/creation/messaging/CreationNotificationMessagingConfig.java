package com.github.kegszool.notification.creation.messaging;

import org.springframework.context.annotation.Bean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import com.github.kegszool.messaging.config.RabbitConfiguration;

@Configuration
public class CreationNotificationMessagingConfig extends RabbitConfiguration {

    private final String requestQueueForDb;
    private final String responseQueueFromDb;
    private final String requestQueueForExchange;
    private final String responseQueueFromExchange;
    private final String notificationCreatedQueue;

    private final String requestForDb;
    private final String responseFromDb;
    private final String requestForExchange;
    private final String responseFromExchange;
    private final String notificationCreated;

    public CreationNotificationMessagingConfig(
            @Value("${spring.rabbitmq.queues.create_notification_request_for_db}") String requestQueueForDb,
            @Value("${spring.rabbitmq.queues.create_notification_response_from_db}") String responseQueueFromDb,
            @Value("${spring.rabbitmq.queues.create_notification_request_for_exchange}") String requestQueueForExchange,
            @Value("${spring.rabbitmq.queues.create_notification_response_from_exchange}") String responseQueueFromExchange,

            @Value("${spring.rabbitmq.template.routing-key.create_notification_request_for_db}") String requestForDb,
            @Value("${spring.rabbitmq.template.routing-key.create_notification_response_from_db}") String responseFromDb,
            @Value("${spring.rabbitmq.template.routing-key.create_notification_request_for_exchange}") String requestForExchange,
            @Value("${spring.rabbitmq.template.routing-key.create_notification_response_from_exchange}") String responseFromExchange,
            @Value("${spring.rabbitmq.queues.notify_created_notification}") String notificationCreatedQueue,
            @Value("${spring.rabbitmq.template.routing-key.notify_created_notification}") String notificationCreated
    ) {
        this.requestQueueForDb = requestQueueForDb;
        this.responseQueueFromDb = responseQueueFromDb;
        this.requestQueueForExchange = requestQueueForExchange;
        this.responseQueueFromExchange = responseQueueFromExchange;
        this.requestForDb = requestForDb;
        this.responseFromDb = responseFromDb;
        this.requestForExchange = requestForExchange;
        this.responseFromExchange = responseFromExchange;
        this.notificationCreatedQueue = notificationCreatedQueue;
        this.notificationCreated = notificationCreated;
    }

    @Bean
    public Queue createNotificationRequestQueueForDb() {
        return new Queue(requestQueueForDb, true, false, false, queueArgs);
    }

    @Bean
    public Queue createNotificationResponseQueueFromDb() {
        return new Queue(responseQueueFromDb, true, false, false, queueArgs);
    }

    @Bean
    public Queue createNotificationRequestQueueForExchange() {
        return new Queue(requestQueueForExchange, true, false, false, queueArgs);
    }

    @Bean
    public Queue createNotificationResponseQueueFromExchange() {
        return new Queue(responseQueueFromExchange,true, false, false, queueArgs);
    }

    @Bean
    public Queue createNotifyCreatedNotificationQueue() {
        return new Queue(notificationCreatedQueue, true, false, false, queueArgs);
    }

    @Bean
    public Binding bindCreationNotificationRequestForDb() {
        return BindingBuilder.bind(createNotificationRequestQueueForDb())
                .to(exchange())
                .with(requestForDb);
    }

    @Bean
    public Binding bindCreationNotificationResponseFromDb() {
        return BindingBuilder.bind(createNotificationResponseQueueFromDb())
                .to(exchange())
                .with(responseFromDb);
    }

    @Bean
    public Binding bindCreationNotificationRequestForExchange() {
        return BindingBuilder.bind(createNotificationRequestQueueForExchange())
                .to(exchange())
                .with(requestForExchange);
    }

    @Bean
    public Binding bindCreationNotificationResponseFromExchange() {
        return BindingBuilder.bind(createNotificationResponseQueueFromExchange())
                .to(exchange())
                .with(responseFromExchange);
    }

    @Bean
    public Binding bindNotifyCreatedNotification() {
        return BindingBuilder.bind(createNotifyCreatedNotificationQueue())
                .to(exchange())
                .with(notificationCreated);
    }
}
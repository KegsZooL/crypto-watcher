package com.github.kegszool.notification.messaging.config;

import org.springframework.context.annotation.Bean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.beans.factory.annotation.Qualifier;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import com.github.kegszool.messaging.config.RabbitConfiguration;

@Configuration
public class CreationNotificationMessagingConfig extends RabbitConfiguration {

    @Bean
    public Queue createNotificationRequestQueueForDb(
            @Value("${spring.rabbitmq.queues.create_notification_request_for_db}") String queueName
    ) {
        return new Queue(queueName, true, false, false, queueArgs);
    }

    @Bean
    public Queue createNotificationResponseQueueFromDb(
            @Value("${spring.rabbitmq.queues.create_notification_response_from_db}") String queueName
    ) {
        return new Queue(queueName, true, false, false, queueArgs);
    }

    @Bean
    public Binding bindCreationNotificationRequestForDb(
            @Value("${spring.rabbitmq.template.routing-key.create_notification_request_for_db}") String routingKey,
            @Qualifier("createNotificationRequestQueueForDb") Queue queue
    ) {
        return BindingBuilder.bind(queue)
                .to(exchange())
                .with(routingKey);
    }

    @Bean
    public Binding bindCreationNotificationResponseFromDb(
            @Value("${spring.rabbitmq.template.routing-key.create_notification_response_from_db}") String routingKey,
            @Qualifier("createNotificationResponseQueueFromDb") Queue queue
    ) {
        return BindingBuilder.bind(queue)
                .to(exchange())
                .with(routingKey);
    }

    @Bean
    public Queue createNotificationRequestQueueForExchange(
            @Value("${spring.rabbitmq.queues.create_notification_request_for_exchange}") String queueName
    ) {
        return new Queue(queueName, true, false, false, queueArgs);
    }

    @Bean
    public Queue createNotificationResponseQueueFromExchange(
            @Value("${spring.rabbitmq.queues.create_notification_response_from_exchange}") String queueName
    ) {
        return new Queue(queueName, true, false, false, queueArgs);
    }

    @Bean
    public Binding bindCreationNotificationRequestForExchange(
            @Value("${spring.rabbitmq.template.routing-key.create_notification_request_for_exchange}") String routingKey,
            @Qualifier("createNotificationRequestQueueForExchange") Queue queue
    ) {
        return BindingBuilder.bind(queue)
                .to(exchange())
                .with(routingKey);
    }

    @Bean
    public Binding bindCreationNotificationResponseFromExchange(
            @Value("${spring.rabbitmq.template.routing-key.create_notification_response_from_exchange}") String routingKey,
            @Qualifier("createNotificationResponseQueueFromExchange") Queue queue
    ) {
        return BindingBuilder.bind(queue)
                .to(exchange())
                .with(routingKey);
    }
}
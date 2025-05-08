package com.github.kegszool.notification.triggered;

import org.springframework.context.annotation.Bean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import com.github.kegszool.messaging.config.RabbitConfiguration;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;

@Configuration
public class TriggeredNotificationMessagingConfig extends RabbitConfiguration {

    private final String routingKey;
    private final String queue;

    private final String afterTriggeredRequestQueue;
    private final String afterTriggeredRequest;
    private final String afterTriggeredResponseQueue;
    private final String afterTriggeredResponse;

    public TriggeredNotificationMessagingConfig(
            @Value("${spring.rabbitmq.template.routing-key.triggered_notification}") String routingKey,
            @Value("${spring.rabbitmq.queues.triggered_notification}") String queue,
            @Value("${spring.rabbitmq.queues.get_user_data_after_triggered_notification_request}") String afterTriggeredRequestQueue,
            @Value("${spring.rabbitmq.template.routing-key.get_user_data_after_triggered_notification_request}") String afterTriggeredRequest,
            @Value("${spring.rabbitmq.queues.get_user_data_after_triggered_notification_response}") String afterTriggeredResponseQueue,
            @Value("${spring.rabbitmq.template.routing-key.get_user_data_after_triggered_notification_response}") String afterTriggeredResponse
    ) {
        this.routingKey = routingKey;
        this.queue = queue;
        this.afterTriggeredRequestQueue = afterTriggeredRequestQueue;
        this.afterTriggeredRequest = afterTriggeredRequest;
        this.afterTriggeredResponseQueue = afterTriggeredResponseQueue;
        this.afterTriggeredResponse = afterTriggeredResponse;
    }

    @Bean
    public Queue triggeredNotificationQueue() {
        return new Queue(queue, true, false, false, queueArgs);
    }

    @Bean
    public Binding bindTriggeredNotification() {
        return BindingBuilder.bind(triggeredNotificationQueue())
                .to(exchange())
                .with(routingKey);
    }

    @Bean
    public Queue afterTriggeredNotificationRequestQueue() {
        return new Queue(afterTriggeredRequestQueue, true, false, false, queueArgs);
    }

    @Bean
    public Binding buildAfterTriggeredRequest() {
        return BindingBuilder.bind(afterTriggeredNotificationRequestQueue())
                .to(exchange())
                .with(afterTriggeredRequest);
    }

    @Bean
    public Queue afterTriggeredNotificationResponseQueue() {
        return new Queue(afterTriggeredResponseQueue, true, false, false, queueArgs);
    }

    @Bean
    public Binding buildAfterTriggeredResponse() {
        return BindingBuilder.bind(afterTriggeredNotificationResponseQueue())
                .to(exchange())
                .with(afterTriggeredResponse);
    }
}
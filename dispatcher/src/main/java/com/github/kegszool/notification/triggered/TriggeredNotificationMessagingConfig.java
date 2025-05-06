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

    public TriggeredNotificationMessagingConfig(
            @Value("${spring.rabbitmq.template.routing-key.triggered_notification}") String routingKey,
            @Value("${spring.rabbitmq.queues.triggered_notification}") String queue
    ) {
        this.routingKey = routingKey;
        this.queue = queue;
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
}
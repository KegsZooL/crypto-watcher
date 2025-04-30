package com.github.kegszool.messaging.config;

import org.springframework.context.annotation.Bean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;

@Configuration
public class ServiceExceptionMessagingConfig extends RabbitConfiguration {

    private final String queue;
    private final String routingKey;

    public ServiceExceptionMessagingConfig(
     	@Value("${spring.rabbitmq.queues.service_exception}") String queue,
     	@Value("${spring.rabbitmq.template.routing-key.service_exception}") String routingKey
    ) {
        this.queue = queue;
        this.routingKey = routingKey;
    }

    @Bean
    public Queue serviceExceptionQueue() {
        return new Queue(queue, true, false, false, queueArgs);
    }

    @Bean
    public Binding bindServiceException() {
        return BindingBuilder.bind(serviceExceptionQueue())
                .to(exchange())
                .with(routingKey);
    }
}
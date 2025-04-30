package com.github.kegszool.language.messaging;

import org.springframework.context.annotation.Bean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import com.github.kegszool.messaging.config.RabbitConfiguration;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;

@Configuration
public class ChangeLanguageMessagingConfig extends RabbitConfiguration {

    private final String requestQueue;
    private final String responseQueue;
    private final String requestRoutingKey;
    private final String responseRoutingKey;

    public ChangeLanguageMessagingConfig(
            @Value("${spring.rabbitmq.queues.change_language_request}") String requestQueue,
            @Value("${spring.rabbitmq.queues.change_language_response}") String responseQueue,
            @Value("${spring.rabbitmq.template.routing-key.change_language_request}") String requestRoutingKey,
            @Value("${spring.rabbitmq.template.routing-key.change_language_response}") String responseRoutingKey
    ) {
        this.requestQueue = requestQueue;
        this.responseQueue = responseQueue;
        this.requestRoutingKey = requestRoutingKey;
        this.responseRoutingKey = responseRoutingKey;
    }

    @Bean
    public Queue changeLanguageRequestQueue() {
        return new Queue(requestQueue, true, false, false, queueArgs);
    }

    @Bean
    public Queue changeLanguageResponseQueue() {
        return new Queue(responseQueue, true, false, false, queueArgs);
    }

    @Bean
    public Binding bindChangeLanguageRequest() {
        return BindingBuilder.bind(changeLanguageRequestQueue())
                .to(exchange())
                .with(requestRoutingKey);
    }

    @Bean
    public Binding bindChangeLanguageResponse() {
        return BindingBuilder.bind(changeLanguageResponseQueue())
                .to(exchange())
                .with(responseRoutingKey);
    }
}
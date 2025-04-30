package com.github.kegszool.user.messaging;

import org.springframework.context.annotation.Bean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import com.github.kegszool.messaging.config.RabbitConfiguration;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;

@Configuration
public class UserMessagingConfig extends RabbitConfiguration {

    private final String requestQueue;
    private final String responseQueue;
    private final String requestRoutingKey;
    private final String responseRoutingKey;

    public UserMessagingConfig(
            @Value("${spring.rabbitmq.queues.upsert_user_request}") String requestQueue,
            @Value("${spring.rabbitmq.queues.upsert_user_response}") String responseQueue,
            @Value("${spring.rabbitmq.template.routing-key.upsert_user_request}") String requestRoutingKey,
            @Value("${spring.rabbitmq.template.routing-key.upsert_user_response}") String responseRoutingKey
    ) {
        this.requestQueue = requestQueue;
        this.responseQueue = responseQueue;
        this.requestRoutingKey = requestRoutingKey;
        this.responseRoutingKey = responseRoutingKey;
    }

    @Bean
    public Queue upsertUserRequestQueue() {
        return new Queue(requestQueue, true, false, false, queueArgs);
    }

    @Bean
    public Queue upsertUserResponseQueue() {
        return new Queue(responseQueue, true, false, false, queueArgs);
    }

    @Bean
    public Binding bindUpsertUserRequest() {
        return BindingBuilder.bind(upsertUserRequestQueue())
                .to(exchange())
                .with(requestRoutingKey);
    }

    @Bean
    public Binding bindUpsertUserResponse() {
        return BindingBuilder.bind(upsertUserResponseQueue())
                .to(exchange())
                .with(responseRoutingKey);
    }
}
package com.github.kegszool.coin.addition.messaging.add;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;

import org.springframework.context.annotation.Bean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import com.github.kegszool.messaging.config.RabbitConfiguration;

@Configuration
public class AdditionCoinMessagingConfig extends RabbitConfiguration {

    private final String requestQueue;
    private final String responseQueue;
    private final String requestRoutingKey;
    private final String responseRoutingKey;

    public AdditionCoinMessagingConfig(
            @Value("${spring.rabbitmq.queues.add_coin_request}") String requestQueue,
            @Value("${spring.rabbitmq.queues.add_coin_response}") String responseQueue,
            @Value("${spring.rabbitmq.template.routing-key.add_coin_request}") String requestRoutingKey,
            @Value("${spring.rabbitmq.template.routing-key.add_coin_response}") String responseRoutingKey
    ) {
        this.requestQueue = requestQueue;
        this.responseQueue = responseQueue;
        this.requestRoutingKey = requestRoutingKey;
        this.responseRoutingKey = responseRoutingKey;
    }

    @Bean
    public Queue addCoinRequestQueue() {
        return new Queue(requestQueue, true, false, false, queueArgs);
    }

    @Bean
    public Queue addCoinResponseQueue() {
        return new Queue(responseQueue, true, false, false, queueArgs);
    }

    @Bean
    public Binding bindAddCoinRequest() {
        return BindingBuilder.bind(addCoinRequestQueue())
                .to(exchange())
                .with(requestRoutingKey);
    }

    @Bean
    public Binding bindAddCoinResponse() {
        return BindingBuilder.bind(addCoinResponseQueue())
                .to(exchange())
                .with(responseRoutingKey);
    }
}
package com.github.kegszool.coin.addition.messaging.check_exists;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;

import org.springframework.context.annotation.Bean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import com.github.kegszool.messaging.config.RabbitConfiguration;

@Configuration
public class CoinExistsMessagingConfig extends RabbitConfiguration {

    private final String requestQueue;
    private final String responseQueue;
    private final String requestRoutingKey;
    private final String responseRoutingKey;

    public CoinExistsMessagingConfig(
            @Value("${spring.rabbitmq.queues.check_coin_exists_request}") String requestQueue,
            @Value("${spring.rabbitmq.queues.check_coin_exists_response}") String responseQueue,
            @Value("${spring.rabbitmq.template.routing-key.check_coin_exists_request}") String requestRoutingKey,
            @Value("${spring.rabbitmq.template.routing-key.check_coin_exists_response}") String responseRoutingKey
    ) {
        this.requestQueue = requestQueue;
        this.responseQueue = responseQueue;
        this.requestRoutingKey = requestRoutingKey;
        this.responseRoutingKey = responseRoutingKey;
    }

    @Bean
    public Queue checkCoinExistsRequestQueue() {
        return new Queue(requestQueue, true, false, false, queueArgs);
    }

    @Bean
    public Queue checkCoinExistsResponseQueue() {
        return new Queue(responseQueue, true, false, false, queueArgs);
    }

    @Bean
    public Binding bindCheckingCoinExistsRequest() {
        return BindingBuilder.bind(checkCoinExistsRequestQueue())
                .to(exchange())
                .with(requestRoutingKey);
    }

    @Bean
    public Binding bindCheckingCoinExistsResponse() {
        return BindingBuilder.bind(checkCoinExistsResponseQueue())
                .to(exchange())
                .with(responseRoutingKey);
    }
}
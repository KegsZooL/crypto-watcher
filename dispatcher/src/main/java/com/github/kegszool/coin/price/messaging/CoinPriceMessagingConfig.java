package com.github.kegszool.coin.price.messaging;

import org.springframework.context.annotation.Bean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import com.github.kegszool.messaging.config.RabbitConfiguration;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;

@Configuration
public class CoinPriceMessagingConfig extends RabbitConfiguration {

    private final String requestQueue;
    private final String responseQueue;
    private final String requestRoutingKey;
    private final String responseRoutingKey;

    public CoinPriceMessagingConfig(
            @Value("${spring.rabbitmq.queues.coin_price_request}") String requestQueue,
            @Value("${spring.rabbitmq.queues.coin_price_response}") String responseQueue,
            @Value("${spring.rabbitmq.template.routing-key.coin_price_request}") String requestRoutingKey,
            @Value("${spring.rabbitmq.template.routing-key.coin_price_response}") String responseRoutingKey
    ) {
        this.requestQueue = requestQueue;
        this.responseQueue = responseQueue;
        this.requestRoutingKey = requestRoutingKey;
        this.responseRoutingKey = responseRoutingKey;
    }

    @Bean
    public Queue coinPriceRequestQueue() {
        return new Queue(requestQueue, true, false, false, queueArgs);
    }

    @Bean
    public Queue coinPriceResponseQueue() {
        return new Queue(responseQueue, true, false, false, queueArgs);
    }

    @Bean
    public Binding bindCoinPriceRequest() {
        return BindingBuilder.bind(coinPriceRequestQueue())
                .to(exchange())
                .with(requestRoutingKey);
    }

    @Bean
    public Binding bindCoinPriceResponse() {
        return BindingBuilder.bind(coinPriceResponseQueue())
                .to(exchange())
                .with(responseRoutingKey);
    }
}
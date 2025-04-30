package com.github.kegszool.coin.deletion.messaging;

import org.springframework.context.annotation.Bean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import com.github.kegszool.messaging.config.RabbitConfiguration;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;

@Configuration
public class DeletionCoinMessagingConfig extends RabbitConfiguration {

    private final String requestQueue;
    private final String responseQueue;
    private final String requestRoutingKey;
    private final String responseRoutingKey;

    public DeletionCoinMessagingConfig(
            @Value("${spring.rabbitmq.queues.delete_favorite_coin_request}") String requestQueue,
         	@Value("${spring.rabbitmq.queues.delete_favorite_coin_response}") String responseQueue,
            @Value("${spring.rabbitmq.template.routing-key.delete_favorite_coin_request}") String requestRoutingKey,
         	@Value("${spring.rabbitmq.template.routing-key.delete_favorite_coin_response}") String responseRoutingKey
    ) {
        this.requestQueue = requestQueue;
        this.responseQueue = responseQueue;
        this.requestRoutingKey = requestRoutingKey;
        this.responseRoutingKey = responseRoutingKey;
    }

    @Bean
    public Queue deleteFavoriteCoinRequestQueue() {
        return new Queue(requestQueue, true, false, false, queueArgs);
    }

    @Bean
    public Queue deleteFavoriteCoinResponseQueue() {
        return new Queue(responseQueue, true, false, false, queueArgs);
    }

    @Bean
    public Binding bindDeleteFavoriteCoinRequest() {
        return BindingBuilder.bind(deleteFavoriteCoinRequestQueue())
                .to(exchange())
                .with(requestRoutingKey);
    }

    @Bean
    public Binding bindDeleteFavoriteCoinResponse() {
        return BindingBuilder.bind(deleteFavoriteCoinResponseQueue())
                .to(exchange())
                .with(responseRoutingKey);
    }
}
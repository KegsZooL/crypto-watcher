package com.github.kegszool.messaging.config;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;

import org.springframework.beans.factory.annotation.Value;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Configuration
public class RabbitConfiguration {

    @Value("${spring.rabbitmq.template.exchange}")
    private String EXCHANGE_NAME;

    @Value("${spring.rabbitmq.queues.request_to_exchange}")
    private String REQUEST_TO_EXCHANGE_QUEUE;

    @Value("${spring.rabbitmq.queues.request_to_database}")
    private String REQUEST_TO_DATABASE_QUEUE;

    @Value("${spring.rabbitmq.queues.response_upsert_user}")
    private String RESPONSE_UPSERT_USER_QUEUE;

    @Value("${spring.rabbitmq.queues.response_delete_favorite_coin}")
    private String RESPONSE_DELETE_FAVORITE_COIN_QUEUE;

    @Value("${spring.rabbitmq.queues.response_coin_price}")
    private String RESPONSE_COIN_PRICE_QUEUE;

    @Value("${spring.rabbitmq.queues.service_exception}")
    private String SERVICE_EXCEPTION_QUEUE;

    @Value("${spring.rabbitmq.template.routing-key.coin_price_request}")
    private String COIN_PRICE_REQUEST_ROUTING_KEY;

    @Value("${spring.rabbitmq.template.routing-key.coin_price_response}")
    private String COIN_PRICE_RESPONSE_ROUTING_KEY;

    @Value("${spring.rabbitmq.template.routing-key.upsert_user_request}")
    private String UPSERT_USER_REQUEST_TO_DATABASE_ROUTING_KEY;

    @Value("${spring.rabbitmq.template.routing-key.upsert_user_response}")
    private String UPSERT_USER_RESPONSE_FROM_DATABASE_ROUTING_KEY;

    @Value("${spring.rabbitmq.template.routing-key.delete_favorite_coin_request}")
    private String DELETE_FAVORITE_COIN_REQUEST_TO_DATABASE_ROUTING_KEY;

    @Value("${spring.rabbitmq.template.routing-key.delete_favorite_coin_response}")
    private String DELETE_FAVORITE_COIN_RESPONSE_FROM_DATABASE_ROUTING_KEY;

    @Value("${spring.rabbitmq.template.routing-key.service_exception}")
    private String SERVICE_EXCEPTION_ROUTING_KEY;

    private final Map<String, Object> queueArgs = Map.of("x-expires", 36000);

    @Bean
    public DirectExchange exchange() {
    	return new DirectExchange(EXCHANGE_NAME, true, false);
    }

    @Bean
    public Queue requestToExchangeQueue() {
        return new Queue(REQUEST_TO_EXCHANGE_QUEUE, true, false, false, queueArgs);
    }

    @Bean
    public Queue requestToDatabaseQueue() {
        return new Queue(REQUEST_TO_DATABASE_QUEUE, true, false, false, queueArgs);
    }

    @Bean
    public Queue upsertUserResponseQueue() {
        return new Queue(RESPONSE_UPSERT_USER_QUEUE, true, false, false, queueArgs);
    }

    @Bean
    public Queue deleteFavoriteCoinResponseQueue() {
        return new Queue(RESPONSE_DELETE_FAVORITE_COIN_QUEUE, true, false, false, queueArgs);
    }

    @Bean
    public Queue coinPriceResponseQueue() {
        return new Queue(RESPONSE_COIN_PRICE_QUEUE, true, false, false, queueArgs);
    }

    @Bean
    public Queue serviceExceptionQueue() {
        return new Queue(SERVICE_EXCEPTION_QUEUE, true, false, false, queueArgs);
    }

    @Bean
    public Binding bindCoinPriceRequestToExchange() {
        return BindingBuilder.bind(requestToExchangeQueue())
                .to(exchange())
                .with(COIN_PRICE_REQUEST_ROUTING_KEY);
    }

    @Bean
    public Binding bindUpsertUserRequestToDatabase() {
        return BindingBuilder.bind(requestToDatabaseQueue())
                .to(exchange())
                .with(UPSERT_USER_REQUEST_TO_DATABASE_ROUTING_KEY);
    }

    @Bean
    public Binding bindDeleteFavoriteCoinRequestToDatabase() {
        return BindingBuilder.bind(requestToDatabaseQueue())
                .to(exchange())
                .with(DELETE_FAVORITE_COIN_REQUEST_TO_DATABASE_ROUTING_KEY);
    }

    @Bean
    public Binding bindUpsertUserResponse() {
        return BindingBuilder.bind(upsertUserResponseQueue())
                .to(exchange())
                .with(UPSERT_USER_RESPONSE_FROM_DATABASE_ROUTING_KEY);
    }

    @Bean
    public Binding bindDeleteFavoriteCoinResponse() {
        return BindingBuilder.bind(deleteFavoriteCoinResponseQueue())
                .to(exchange())
                .with(DELETE_FAVORITE_COIN_RESPONSE_FROM_DATABASE_ROUTING_KEY);
    }

    @Bean
    public Binding bindCoinPriceResponse() {
        return BindingBuilder.bind(coinPriceResponseQueue())
                .to(exchange())
                .with(COIN_PRICE_RESPONSE_ROUTING_KEY);
    }

    @Bean
    public Binding bindServiceException() {
        return BindingBuilder.bind(coinPriceResponseQueue())
                .to(exchange())
                .with(SERVICE_EXCEPTION_ROUTING_KEY);
    }

    @Bean
    public Jackson2JsonMessageConverter jackson2JsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}
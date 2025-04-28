package com.github.kegszool.messaging.config;

import org.springframework.context.annotation.Bean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;

import java.util.Map;

@Configuration
public class RabbitConfiguration {

    private final Map<String, Object> queueArgs = Map.of("x-expires", 36000);

    @Value("${spring.rabbitmq.template.exchange}")
    private String EXCHANGE_NAME;

    @Value("${spring.rabbitmq.queues.upsert_user_request}")
    private String UPSERT_USER_REQUEST_QUEUE;

    @Value("${spring.rabbitmq.queues.upsert_user_response}")
    private String UPSERT_USER_RESPONSE_QUEUE;

    @Value("${spring.rabbitmq.template.routing-key.upsert_user_request}")
    private String UPSERT_USER_REQUEST_ROUTING_KEY;

    @Value("${spring.rabbitmq.template.routing-key.upsert_user_response}")
    private String UPSERT_USER_RESPONSE_ROUTING_KEY;

    @Value("${spring.rabbitmq.queues.delete_favorite_coin_request}")
    private String DELETE_FAVORITE_COIN_REQUEST_QUEUE;

    @Value("${spring.rabbitmq.queues.delete_favorite_coin_response}")
    private String DELETE_FAVORITE_COIN_RESPONSE_QUEUE;

    @Value("${spring.rabbitmq.template.routing-key.delete_favorite_coin_request}")
    private String DELETE_FAVORITE_COIN_REQUEST_ROUTING_KEY;

    @Value("${spring.rabbitmq.template.routing-key.delete_favorite_coin_response}")
    private String DELETE_FAVORITE_COIN_RESPONSE_ROUTING_KEY;

    @Value("${spring.rabbitmq.queues.coin_price_request}")
    private String COIN_PRICE_REQUEST_QUEUE;

    @Value("${spring.rabbitmq.queues.coin_price_response}")
    private String COIN_PRICE_RESPONSE_QUEUE;

    @Value("${spring.rabbitmq.template.routing-key.coin_price_request}")
    private String COIN_PRICE_REQUEST_ROUTING_KEY;

    @Value("${spring.rabbitmq.template.routing-key.coin_price_response}")
    private String COIN_PRICE_RESPONSE_ROUTING_KEY;

    @Value("${spring.rabbitmq.queues.check_coin_exists_request}")
    private String CHECK_COIN_EXISTS_REQUEST_QUEUE;

    @Value("${spring.rabbitmq.queues.check_coin_exists_response}")
    private String CHECK_COIN_EXISTS_RESPONSE_QUEUE;

    @Value("${spring.rabbitmq.template.routing-key.check_coin_exists_request}")
    private String CHECK_COIN_EXISTS_REQUEST_ROUTING_KEY;

    @Value("${spring.rabbitmq.template.routing-key.check_coin_exists_response}")
    private String CHECK_COIN_EXISTS_RESPONSE_ROUTING_KEY;

    @Value("${spring.rabbitmq.queues.add_coin_request}")
    private String ADD_COIN_REQUEST_QUEUE;

    @Value("${spring.rabbitmq.queues.add_coin_response}")
    private String ADD_COIN_RESPONSE_QUEUE;

    @Value("${spring.rabbitmq.template.routing-key.add_coin_request}")
    private String ADD_COIN_REQUEST_ROUTING_KEY;

    @Value("${spring.rabbitmq.template.routing-key.add_coin_response}")
    private String ADD_COIN_RESPONSE_ROUTING_KEY;

    @Value("${spring.rabbitmq.queues.change_language_request}")
    private String CHANGE_LANGUAGE_REQUEST_QUEUE;

    @Value("${spring.rabbitmq.queues.change_language_response}")
    private String CHANGE_LANGUAGE_RESPONSE_QUEUE;

    @Value("${spring.rabbitmq.template.routing-key.change_language_request}")
    private String CHANGE_LANGUAGE_REQUEST_ROUTING_KEY;

    @Value("${spring.rabbitmq.template.routing-key.change_language_response}")
    private String CHANGE_LANGUAGE_RESPONSE_ROUTING_KEY;

    @Value("${spring.rabbitmq.queues.service_exception}")
    private String SERVICE_EXCEPTION_QUEUE;

    @Value("${spring.rabbitmq.template.routing-key.service_exception}")
    private String SERVICE_EXCEPTION_ROUTING_KEY;

    @Bean
    public DirectExchange exchange() {
    	return new DirectExchange(EXCHANGE_NAME, true, false);
    }

    @Bean
    public Queue upsertUserRequestQueue() {
        return new Queue(UPSERT_USER_REQUEST_QUEUE, true, false, false, queueArgs);
    }

    @Bean
    public Queue upsertUserResponseQueue() {
        return new Queue(UPSERT_USER_RESPONSE_QUEUE, true, false, false, queueArgs);
    }

    @Bean
    public Queue deleteFavoriteCoinRequestQueue() {
        return new Queue(DELETE_FAVORITE_COIN_REQUEST_QUEUE, true, false, false, queueArgs);
    }

    @Bean
    public Queue deleteFavoriteCoinResponseQueue() {
        return new Queue(DELETE_FAVORITE_COIN_RESPONSE_QUEUE, true, false, false, queueArgs);
    }

    @Bean
    public Queue coinPriceRequestQueue() {
        return new Queue(COIN_PRICE_REQUEST_QUEUE, true, false, false, queueArgs);
    }

    @Bean
    public Queue coinPriceResponseQueue() {
        return new Queue(COIN_PRICE_RESPONSE_QUEUE, true, false, false, queueArgs);
    }

    @Bean
    public Queue checkCoinExistsRequestQueue() {
        return new Queue(CHECK_COIN_EXISTS_REQUEST_QUEUE, true, false, false, queueArgs);
    }

    @Bean
    public Queue checkCoinExistsResponseQueue() {
        return new Queue(CHECK_COIN_EXISTS_RESPONSE_QUEUE, true, false, false, queueArgs);
    }

    @Bean
    public Queue addCoinRequestQueue() {
        return new Queue(ADD_COIN_REQUEST_QUEUE, true, false, false, queueArgs);
    }

    @Bean
    public Queue addCoinResponseQueue() {
        return new Queue(ADD_COIN_RESPONSE_QUEUE, true, false, false, queueArgs);
    }

    @Bean
    public Queue changeLanguageRequestQueue() {
        return new Queue(CHANGE_LANGUAGE_REQUEST_QUEUE, true, false, false, queueArgs);
    }

    @Bean
    public Queue changeLanguageResponseQueue() {
        return new Queue(CHANGE_LANGUAGE_RESPONSE_QUEUE, true, false, false, queueArgs);
    }

    @Bean
    public Queue serviceExceptionQueue() {
        return new Queue(SERVICE_EXCEPTION_QUEUE, true, false, false, queueArgs);
    }

    @Bean
    public Binding bindUpsertUserRequest() {
        return BindingBuilder.bind(upsertUserRequestQueue())
                .to(exchange())
                .with(UPSERT_USER_REQUEST_ROUTING_KEY);
    }

    @Bean
    public Binding bindUpsertUserResponse() {
        return BindingBuilder.bind(upsertUserResponseQueue())
                .to(exchange())
                .with(UPSERT_USER_RESPONSE_ROUTING_KEY);
    }

    @Bean
    public Binding bindDeleteFavoriteCoinRequest() {
        return BindingBuilder.bind(deleteFavoriteCoinRequestQueue())
                .to(exchange())
                .with(DELETE_FAVORITE_COIN_REQUEST_ROUTING_KEY);
    }

    @Bean
    public Binding bindDeleteFavoriteCoinResponse() {
        return BindingBuilder.bind(deleteFavoriteCoinResponseQueue())
                .to(exchange())
                .with(DELETE_FAVORITE_COIN_RESPONSE_ROUTING_KEY);
    }

    @Bean
    public Binding bindCoinPriceRequest() {
        return BindingBuilder.bind(coinPriceRequestQueue())
                .to(exchange())
                .with(COIN_PRICE_REQUEST_ROUTING_KEY);
    }

    @Bean
    public Binding bindCoinPriceResponse() {
        return BindingBuilder.bind(coinPriceResponseQueue())
                .to(exchange())
                .with(COIN_PRICE_RESPONSE_ROUTING_KEY);
    }

    @Bean
    public Binding bindCheckingCoinExistsRequest() {
        return BindingBuilder.bind(checkCoinExistsRequestQueue())
                .to(exchange())
                .with(CHECK_COIN_EXISTS_REQUEST_ROUTING_KEY);
    }

    @Bean
    public Binding bindCheckingCoinExistsResponse() {
        return BindingBuilder.bind(checkCoinExistsResponseQueue())
                .to(exchange())
                .with(CHECK_COIN_EXISTS_RESPONSE_ROUTING_KEY);
    }

    @Bean
    public Binding bindAddCoinRequest() {
        return BindingBuilder.bind(addCoinRequestQueue())
                .to(exchange())
                .with(ADD_COIN_REQUEST_ROUTING_KEY);
    }

    @Bean
    public Binding bindAddCoinResponse() {
        return BindingBuilder.bind(addCoinResponseQueue())
                .to(exchange())
                .with(ADD_COIN_RESPONSE_ROUTING_KEY);
    }

    @Bean
    public Binding bindChangeLanguageRequest() {
        return BindingBuilder.bind(changeLanguageRequestQueue())
                .to(exchange())
                .with(CHANGE_LANGUAGE_REQUEST_ROUTING_KEY);
    }

    @Bean
    public Binding bindChangeLanguageResponse() {
        return BindingBuilder.bind(changeLanguageResponseQueue())
                .to(exchange())
                .with(CHANGE_LANGUAGE_RESPONSE_ROUTING_KEY);
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
package com.github.kegszool.configuration;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfiguration {

    @Value("${spring.rabbitmq.template.exchange}")
    private String EXCHANGE_NAME;

    @Value("${spring.rabbitmq.queues.request_to_exchange_queue}")
    private String REQUEST_TO_EXCHANGE_QUEUE;

    @Value("${spring.rabbitmq.queues.response_from_exchange_queue}")
    private String RESPONSE_FROM_EXCHANGE_QUEUE;

    @Value("${spring.rabbitmq.queues.notification_queue}")
    private String NOTIFICATION_QUEUE;

    @Value("${spring.rabbitmq.template.routing-key.coin_price_key}")
    private String COIN_PRICE_ROUTING_KEY;

    @Bean
    public DirectExchange exchange() {
        return new DirectExchange(EXCHANGE_NAME, true, false);
    }

    @Bean
    public Queue requestToExchangeQueue() {
        return new Queue(REQUEST_TO_EXCHANGE_QUEUE);
    }

    @Bean
    public Queue responseFromExchangeQueue() {
        return new Queue(RESPONSE_FROM_EXCHANGE_QUEUE);
    }

    @Bean
    public Queue notificationQueue() {
        return new Queue(NOTIFICATION_QUEUE);
    }

    @Bean
    public Binding bindingRequestToExchange() {
        return BindingBuilder.bind(requestToExchangeQueue())
                .to(exchange())
                .with(REQUEST_TO_EXCHANGE_QUEUE);
    }

    @Bean
    public Binding bindingResponsePriceFromExchange() {
        return BindingBuilder.bind(responseFromExchangeQueue())
                .to(exchange())
                .with(COIN_PRICE_ROUTING_KEY);
    }

    @Bean
    public Binding bindingNotification() {
        return BindingBuilder.bind(requestToExchangeQueue())
                .to(exchange())
                .with(NOTIFICATION_QUEUE);
    }
}
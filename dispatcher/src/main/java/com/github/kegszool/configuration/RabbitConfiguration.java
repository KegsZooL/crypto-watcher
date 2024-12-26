package com.github.kegszool.configuration;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfiguration {

    @Value("${spring.rabbitmq.template.exchange}")
    private String EXCHANGE_NAME;

    @Value("${spring.rabbitmq.queues.request_queue}")
    private String COIN_REQUEST_QUEUE;

    @Value("${spring.rabbitmq.queues.response_queue}")
    private String COIN_RESPONSE_QUEUE;

    @Value("${spring.rabbitmq.queues.notification_queue}")
    private String NOTIFICATION_QUEUE;

    @Bean
    public DirectExchange exchange() {
        return new DirectExchange(EXCHANGE_NAME, true, false);
    }

    @Bean
    public Queue coinRequestQueue() {
        return new Queue(COIN_REQUEST_QUEUE);
    }

    @Bean
    public Queue coinResponseQueue() {
        return new Queue(COIN_RESPONSE_QUEUE);
    }

    @Bean
    public Queue notificationQueue() {
        return new Queue(NOTIFICATION_QUEUE);
    }

    @Bean
    public Binding bindingCoinRequestQueue() {
        return BindingBuilder.bind(coinRequestQueue()).to(exchange()).with(COIN_REQUEST_QUEUE);
    }

    @Bean
    public Binding bindingCoinResponseQueue() {
        return BindingBuilder.bind(coinResponseQueue()).to(exchange()).with(COIN_RESPONSE_QUEUE);
    }

    @Bean
    public Binding bindingNotificaitonQueue() {
        return BindingBuilder.bind(coinRequestQueue()).to(exchange()).with(NOTIFICATION_QUEUE);
    }

    @Bean
    public MessageConverter messageConverter() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        return new Jackson2JsonMessageConverter(objectMapper);
    }

}
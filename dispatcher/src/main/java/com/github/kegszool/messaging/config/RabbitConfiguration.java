package com.github.kegszool.messaging.config;

import java.util.Map;
import org.springframework.context.annotation.Bean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;

@Configuration
public abstract class RabbitConfiguration {

    protected final Map<String, Object> queueArgs = Map.of("x-expires", 36000);

    @Value("${spring.rabbitmq.template.exchange}")
    private String EXCHANGE_NAME;

    @Bean
    public DirectExchange exchange() {
    	return new DirectExchange(EXCHANGE_NAME, true, false);
    }

    @Bean
    public Jackson2JsonMessageConverter jackson2JsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}
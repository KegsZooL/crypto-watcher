package com.github.kegszool.messaging.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;

@Configuration
public class RabbitConfiguration {

    @Bean(name = "jsonMessageConverterForDataManager")
    public Jackson2JsonMessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}
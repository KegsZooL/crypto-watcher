package com.github.kegszool.communication_service;

import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Header;

public interface ResponseConsumerService {
    void consume(String response, @Header(AmqpHeaders.RECEIVED_ROUTING_KEY) String routingKey);
}
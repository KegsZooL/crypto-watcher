package com.github.kegszool.messaging.consumer;

import com.github.kegszool.messaging.dto.service.ServiceMessage;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Header;

public interface ResponseConsumerService<T> {
    void consume(ServiceMessage<T> serviceMessage, @Header(AmqpHeaders.RECEIVED_ROUTING_KEY) String routingKey);
}
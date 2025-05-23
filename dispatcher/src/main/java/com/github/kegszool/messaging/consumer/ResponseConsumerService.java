package com.github.kegszool.messaging.consumer;

import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Header;
import com.github.kegszool.messaging.dto.service.ServiceMessage;

public interface ResponseConsumerService<T> {
    void consume(ServiceMessage<T> serviceMessage, @Header(AmqpHeaders.RECEIVED_ROUTING_KEY) String routingKey);
}
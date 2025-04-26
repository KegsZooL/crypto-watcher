package com.github.kegszool.messaging.consumer;

import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Header;
import com.github.kegszool.messaging.dto.service.ServiceMessage;

public interface RequestConsumer<I> {
    void consume(ServiceMessage<I> serviceMessage, @Header(AmqpHeaders.RECEIVED_ROUTING_KEY) String routingKey);
}
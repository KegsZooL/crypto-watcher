package com.github.kegszool.messaging.consumer;

import com.github.kegszool.messaging.dto.service.ServiceMessage;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Header;

public interface RequestConsumerService {
    void consume(ServiceMessage<String> serviceMessage, @Header(AmqpHeaders.RECEIVED_ROUTING_KEY) String routingKey);
}
package com.github.kegszool.communication_service;

import com.github.kegszool.DTO.DataTransferObject;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Header;

public interface RequestConsumerService {
    void consume(DataTransferObject dataTransferObject, @Header(AmqpHeaders.RECEIVED_ROUTING_KEY) String routingKey);
}
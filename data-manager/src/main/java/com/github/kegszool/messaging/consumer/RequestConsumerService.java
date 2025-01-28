package com.github.kegszool.messaging.consumer;

import com.github.kegszool.db.entity.dto.BaseEntityDto;
import com.github.kegszool.messaging.dto.ServiceMessage;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Header;

public interface RequestConsumerService {
    void consume(ServiceMessage<BaseEntityDto> serviceMessage, @Header(AmqpHeaders.RECEIVED_ROUTING_KEY) String routingKey);
}

package com.github.kegszool.messaging.consumer;

import com.github.kegszool.messaging.dto.service.ServiceMessage;

public interface RequestConsumer<T> {
    void consume(ServiceMessage<T> serviceMessage, String routingKey);
}
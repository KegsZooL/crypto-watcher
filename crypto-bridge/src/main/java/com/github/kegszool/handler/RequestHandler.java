package com.github.kegszool.handler;

import com.github.kegszool.messaging.dto.ServiceMessage;
import com.github.kegszool.messaging.producer.ResponseProducerService;

public interface RequestHandler {
    boolean canHandle(String routingKey);
    void handle(ServiceMessage serviceMessage, ResponseProducerService responseProducerService);
}

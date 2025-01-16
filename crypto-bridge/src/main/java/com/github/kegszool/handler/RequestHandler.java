package com.github.kegszool.handler;

import com.github.kegszool.messaging.dto.ServiceMessage;
import com.github.kegszool.messaging.producer.ResponseProducerService;

public interface RequestHandler {
    boolean canHandle(String routingKey);
    String getResponseRoutingKey();
    String handle(ServiceMessage serviceMessage);
}

package com.github.kegszool.handler;

import com.github.kegszool.messaging.dto.ServiceMessage;

public interface RequestHandler {
    boolean canHandle(String routingKey);
    String getResponseRoutingKey();
    ServiceMessage<?> handle(ServiceMessage<String> serviceMessage);
}

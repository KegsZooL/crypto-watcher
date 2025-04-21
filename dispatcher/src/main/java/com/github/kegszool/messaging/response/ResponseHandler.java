package com.github.kegszool.messaging.response;

import com.github.kegszool.messaging.dto.HandlerResult;
import com.github.kegszool.messaging.dto.ServiceMessage;

public interface ResponseHandler<T> {
    boolean canHandle(String routingKey);
    HandlerResult handle(ServiceMessage<T> serviceMessage);
}
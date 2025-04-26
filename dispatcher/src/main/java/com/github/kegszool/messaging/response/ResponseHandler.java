package com.github.kegszool.messaging.response;

import com.github.kegszool.messaging.dto.HandlerResult;
import com.github.kegszool.messaging.dto.service.ServiceMessage;

public interface ResponseHandler<I> {
    boolean canHandle(String routingKey);
    HandlerResult handle(ServiceMessage<I> serviceMessage);
}
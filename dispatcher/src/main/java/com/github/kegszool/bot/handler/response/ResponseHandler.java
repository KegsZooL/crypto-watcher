package com.github.kegszool.bot.handler.response;

import com.github.kegszool.bot.handler.HandlerResult;
import com.github.kegszool.messaging.dto.service.ServiceMessage;

public interface ResponseHandler<T> {
    boolean canHandle(String routingKey);
    HandlerResult handle(ServiceMessage<T> serviceMessage);
}
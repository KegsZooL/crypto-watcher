package com.github.kegszool.handler;

import com.github.kegszool.messaging.dto.ServiceMessage;

public interface RequestHandler {
    ServiceMessage<?> handle(ServiceMessage<String> serviceMessage);
}
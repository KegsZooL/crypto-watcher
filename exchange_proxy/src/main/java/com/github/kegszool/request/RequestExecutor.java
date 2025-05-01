package com.github.kegszool.request;

import com.github.kegszool.messaging.dto.service.ServiceMessage;

public interface RequestExecutor<T, O> {
    ServiceMessage<O> execute(ServiceMessage<T> serviceMessage);
    String getResponseRoutingKey();
}
package com.github.kegszool.messaging;


import com.github.kegszool.messaging.dto.service.ServiceMessage;

public interface RequestExecutor<I, O> {
    ServiceMessage<O> execute(ServiceMessage<I> serviceMessage);
    String getResponseRoutingKey();
}
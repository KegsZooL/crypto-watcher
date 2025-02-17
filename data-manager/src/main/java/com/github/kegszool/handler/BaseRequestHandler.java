package com.github.kegszool.handler;

import com.github.kegszool.utils.ServiceMessageUtils;
import com.github.kegszool.handler.impl.RequestHandler;
import com.github.kegszool.messaging.dto.service.ServiceMessage;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

@Log4j2
@Component
public abstract class BaseRequestHandler<T> implements RequestHandler<T> {

    public ServiceMessage<?> handleRequest(ServiceMessage<?> serviceMessage) {
        var mappedServiceMessage = ServiceMessageUtils.mapToServiceMessage(serviceMessage, getDataClass());
        return handle(mappedServiceMessage);
    }
    public abstract String getRequestRoutingKey();
    public abstract String getResponseRoutingKey();
    public abstract Class<T> getDataClass();
}
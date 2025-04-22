package com.github.kegszool.utils;

import com.github.kegszool.handler.BaseRequestHandler;
import com.github.kegszool.messaging.dto.ServiceMessage;
import com.github.kegszool.exception.handler.RequestHandlerNotFoundException;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

@Log4j2
@Component
public class RequestHandlerFactory {

    private final Map<String, BaseRequestHandler> routingKeyHandlerMatching = new ConcurrentHashMap<>();

    public RequestHandlerFactory(List<BaseRequestHandler> handlers) {
        handlers.forEach(handler -> routingKeyHandlerMatching
                .put(handler.getRequestRoutingKey(), handler)
        );
    }

    public BaseRequestHandler getHandler(ServiceMessage<?> serviceMessage, String routingKey) {
        return Optional.ofNullable(routingKeyHandlerMatching.get(routingKey))
                .orElseThrow(() -> processMissingHandler(serviceMessage, routingKey));
    }

    private RequestHandlerNotFoundException processMissingHandler(ServiceMessage<?> serviceMessage, String routingKey) {
        log.warn("The request handler for this routing key: \"{}\" was not found", routingKey);

        var data = serviceMessage.getData();
        var chatId  = serviceMessage.getChatId();
        var exceptionMsg = String.format("Routing key: \"%s\". Data: \"%s\". ChatId: \"%s\" ",
                routingKey, data, chatId);

        return new RequestHandlerNotFoundException(exceptionMsg);
    }
}
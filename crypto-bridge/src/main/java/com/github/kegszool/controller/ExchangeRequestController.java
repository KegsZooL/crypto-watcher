package com.github.kegszool.controller;

import com.github.kegszool.messaging.dto.ServiceMessage;
import com.github.kegszool.messaging.producer.ResponseProducerService;
import com.github.kegszool.handler.RequestHandler;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Log4j2
public class ExchangeRequestController {

    private final List<RequestHandler> requestHandlers;
    private final ResponseProducerService responseProducerService;

    @Autowired
    public ExchangeRequestController(
            List<RequestHandler> requestHandlers,
            ResponseProducerService responseProducerService) {
        this.requestHandlers = requestHandlers;
        this.responseProducerService = responseProducerService;
    }

    //TODO: ДОБАВИТЬ ЛОГИРОВАНИЕ + ИСКЛЮЧЕНИЕ
    public void handle(ServiceMessage serviceMessage, String routingKey) {
        requestHandlers.stream()
                .filter(requestHandler -> requestHandler.canHandle(routingKey))
                .findFirst()
                .ifPresent(requestHandler -> requestHandler.handle(serviceMessage, responseProducerService));
    }
}
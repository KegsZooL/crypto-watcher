package com.github.kegszool.controller;

import com.github.kegszool.DTO.DataTransferObject;
import com.github.kegszool.communication_service.ResponseProducerService;
import com.github.kegszool.request_handler.RequestHandler;
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
    public void handle(DataTransferObject dataTransferObject, String routingKey) {
        requestHandlers.stream()
                .filter(requestHandler -> requestHandler.canHandle(routingKey))
                .findFirst()
                .ifPresent(requestHandler -> requestHandler.handle(dataTransferObject, responseProducerService));
    }
}
package com.github.kegszool.controller;

import com.github.kegszool.exception.exchange.request.ExchangeRequestException;
import com.github.kegszool.exception.exchange.request.handler.ExchangeRequestHandlerNotFoundException;
import com.github.kegszool.messaging.dto.ServiceMessage;
import com.github.kegszool.messaging.producer.ResponseProducerService;
import com.github.kegszool.handler.RequestHandler;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;


//TODO: rewrite the proxy server impl

@Component
@Log4j2
public class ExchangeRequestController {

    private final List<RequestHandler> requestHandlers;
    private final ResponseProducerService responseProducerService;

    @Autowired
    public ExchangeRequestController(
            List<RequestHandler> requestHandlers,
            ResponseProducerService responseProducerService
    ) {
        this.requestHandlers = requestHandlers;
        this.responseProducerService = responseProducerService;
    }

    public void handle(ServiceMessage serviceMessage, String routingKey) {
        try {
           RequestHandler handler = requestHandlers.stream()
                   .filter(requestHandler -> requestHandler.canHandle(routingKey))
                   .findFirst()
                   .orElseThrow(() -> processMissingHandler(serviceMessage, routingKey));

           String response = handler.handle(serviceMessage);
           String responseRoutingKey = handler.getResponseRoutingKey();

           serviceMessage.setData(response);
           responseProducerService.produce(serviceMessage, responseRoutingKey);

        } catch(ExchangeRequestException ex) {
            //TODO: Think about handaling the exception. Should I notify the user?
        }
    }

    private ExchangeRequestHandlerNotFoundException processMissingHandler(ServiceMessage serviceMessage, String routingKey) {
        log.warn("The request handler for this routing key: \"{}\" was not found", routingKey);

        var data = serviceMessage.getData();
        var chatId  = serviceMessage.getChatId();
        var exceptionMsg = String.format("Routing key: \"%s\". Data: \"%s\". ChatId: \"%s\" ",
                routingKey, data, chatId);

        return new ExchangeRequestHandlerNotFoundException(exceptionMsg);
    }
}
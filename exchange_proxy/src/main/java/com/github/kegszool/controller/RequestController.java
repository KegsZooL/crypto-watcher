package com.github.kegszool.controller;//package com.github.kegszool.controller;

import com.github.kegszool.exception.request.RequestException;
import com.github.kegszool.exception.handler.RequestHandlerNotFoundException;
import com.github.kegszool.messaging.dto.ServiceException;
import com.github.kegszool.messaging.dto.ServiceMessage;
import com.github.kegszool.messaging.producer.ResponseProducerService;
import com.github.kegszool.handler.RequestHandler;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Log4j2
public class RequestController {

    @Value ("${spring.rabbitmq.template.routing-key.service_exception}")
    private String SERVICE_EXCEPTION_ROUTING_KEY;

    private final List<RequestHandler> requestHandlers;
    private final ResponseProducerService responseProducerService;

    @Autowired
    public RequestController(
            List<RequestHandler> requestHandlers,
            ResponseProducerService responseProducerService
    ) {
        this.requestHandlers = requestHandlers;
        this.responseProducerService = responseProducerService;
    }

    public void handle(ServiceMessage<String> serviceMessage, String routingKey) {
        try {
           RequestHandler handler = requestHandlers.stream()
                   .filter(requestHandler -> requestHandler.canHandle(routingKey))
                   .findFirst()
                   .orElseThrow(() -> processMissingHandler(serviceMessage, routingKey));

           ServiceMessage<?> responseServiceMessage = handler.handle(serviceMessage);
           String responseRoutingKey = handler.getResponseRoutingKey();

           responseProducerService.produce(responseServiceMessage, responseRoutingKey);

        } catch(RequestException ex) { handleServiceException(ex, routingKey, serviceMessage.getMessageId(), serviceMessage.getChatId()); }
    }

    private RequestHandlerNotFoundException processMissingHandler(ServiceMessage serviceMessage, String routingKey) {
        log.warn("The request handler for this routing key: \"{}\" was not found", routingKey);

        var data = serviceMessage.getData();
        var chatId  = serviceMessage.getChatId();
        var exceptionMsg = String.format("Routing key: \"%s\". Data: \"%s\". ChatId: \"%s\" ",
                routingKey, data, chatId);

        return new RequestHandlerNotFoundException(exceptionMsg);
    }

    private void handleServiceException(RequestException ex, String routingKey, Integer messageId, String chatId) {
        log.error("Error while handling request for routing key: {}", routingKey, ex);

        String exceptionName = ex.getClass().getSimpleName();
        String exceptionMessage = ex.getMessage();
        var serviceException = new ServiceException(exceptionName, exceptionMessage);

        ServiceMessage<ServiceException> serviceMessage = new ServiceMessage(messageId, chatId, serviceException);
        responseProducerService.produce(serviceMessage, SERVICE_EXCEPTION_ROUTING_KEY);
    }
}
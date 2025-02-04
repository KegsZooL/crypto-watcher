package com.github.kegszool.controller;

import com.github.kegszool.exception.request.RequestException;
import com.github.kegszool.exception.handler.RequestHandlerNotFoundException;
import com.github.kegszool.exception.service.ServiceException;
import com.github.kegszool.messaging.dto.service.ServiceMessage;
import com.github.kegszool.messaging.producer.ResponseProducerService;
import com.github.kegszool.handler.RequestHandler;
import com.github.kegszool.utils.RequestHandlerFactory;
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

    private final RequestHandlerFactory requestHandlerFactory;
    private final ResponseProducerService responseProducerService;

    @Autowired
    public RequestController(
            RequestHandlerFactory requestHandlerFactory,
            ResponseProducerService responseProducerService
    ) {
        this.requestHandlerFactory = requestHandlerFactory;
        this.responseProducerService = responseProducerService;
    }

    public void handle(ServiceMessage<String> serviceMessage, String routingKey) {
        try {
           RequestHandler handler = requestHandlerFactory.getHandler(serviceMessage, routingKey);

           ServiceMessage<?> responseServiceMessage = handler.handle(serviceMessage);
           String responseRoutingKey = handler.getResponseRoutingKey();

           responseProducerService.produce(responseServiceMessage, responseRoutingKey);

        } catch(RequestException ex) { handleServiceException(ex, routingKey, serviceMessage.getMessageId(), serviceMessage.getChatId()); }
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
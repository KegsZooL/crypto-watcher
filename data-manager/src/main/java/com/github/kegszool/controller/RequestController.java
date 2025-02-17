package com.github.kegszool.controller;

import com.github.kegszool.utils.RequestHandlerFactory;
import com.github.kegszool.handler.BaseRequestHandler;
import com.github.kegszool.exception.service.ServiceException;

import com.github.kegszool.messaging.dto.service.ServiceMessage;
import com.github.kegszool.messaging.producer.ResponseProducerService;

import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import lombok.extern.log4j.Log4j2;

@Log4j2
@Component
public class RequestController {

    @Value("${spring.rabbitmq.template.routing-key.service_exception}")
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

    public void handle(ServiceMessage<?> serviceMessage, String routingKey) {
        try {
            BaseRequestHandler<?> handler = requestHandlerFactory.getHandler(serviceMessage, routingKey);

            ServiceMessage<?> responseServiceMessage = handler.handleRequest(serviceMessage);
            String responseRoutingKey = handler.getResponseRoutingKey();

            responseProducerService.produce(responseServiceMessage, responseRoutingKey);

        } catch (Exception ex) {
            handleServiceException(ex, routingKey, serviceMessage.getMessageId(), serviceMessage.getChatId());
        }
    }

    private void handleServiceException(Exception ex, String routingKey, Integer messageId, String chatId) {
        log.error("Error while handling request for routing key: {}", routingKey, ex);

        String exceptionName = ex.getClass().getSimpleName();
        String exceptionMessage = ex.getMessage();
        var serviceException = new ServiceException(exceptionName, exceptionMessage);

        ServiceMessage<ServiceException> serviceMessage = new ServiceMessage(messageId, chatId, serviceException);
        responseProducerService.produce(serviceMessage, SERVICE_EXCEPTION_ROUTING_KEY);
    }
}
package com.github.kegszool.messaging.consumer;

import com.github.kegszool.utils.ServiceMessageUtils;
import com.github.kegszool.controller.RequestController;
import com.github.kegszool.messaging.dto.ServiceMessage;

import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.amqp.rabbit.annotation.RabbitListener;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Header;

import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
public class RequestConsumer implements RequestConsumerService {

    private final RequestController requestController;

    @Autowired
    public RequestConsumer(RequestController requestController) {
        this.requestController = requestController;
    }

    @Override
    @RabbitListener(queues = "${spring.rabbitmq.queues.request_to_exchange}")
    public void consume(ServiceMessage<String> serviceMessage, @Header(AmqpHeaders.RECEIVED_ROUTING_KEY) String routingKey) {
        if (ServiceMessageUtils.isDataValid(serviceMessage, routingKey)) {
            ServiceMessageUtils.logReceivedRequest(serviceMessage, routingKey);
            requestController.handle(serviceMessage, routingKey);
        } else {
            throw ServiceMessageUtils.handleInvalidServiceMessage(serviceMessage, routingKey);
        }
    }
}
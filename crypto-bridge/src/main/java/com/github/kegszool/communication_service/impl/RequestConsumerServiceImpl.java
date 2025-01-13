package com.github.kegszool.communication_service.impl;

import com.github.kegszool.DTO.DataTransferObject;
import com.github.kegszool.controller.ExchangeRequestController;
import com.github.kegszool.communication_service.RequestConsumerService;
import lombok.extern.log4j.Log4j2;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;

//TODO: доделать логирование

@Service
@Log4j2
public class RequestConsumerServiceImpl implements RequestConsumerService {

    private final ExchangeRequestController exchangeRequestController;

    @Autowired
    public RequestConsumerServiceImpl(ExchangeRequestController exchangeRequestController) {
        this.exchangeRequestController = exchangeRequestController;
    }

    @Override
    @RabbitListener(queues = "${spring.rabbitmq.queues.request_to_exchange_queue}")
    public void consume(DataTransferObject dataTransferObject, @Header(AmqpHeaders.RECEIVED_ROUTING_KEY) String routingKey) {
//        log.info("The request was received: {}", routingKey);
        exchangeRequestController.handle(dataTransferObject, routingKey);
    }
}

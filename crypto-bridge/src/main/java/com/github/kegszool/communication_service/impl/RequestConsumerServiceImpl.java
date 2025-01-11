package com.github.kegszool.communication_service.impl;

import com.github.kegszool.controller.ExchangeRequestController;
import com.github.kegszool.communication_service.RequestConsumerService;
import lombok.extern.log4j.Log4j2;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    public void consume(String request) {
        log.info("The request was received: {}", request);
        exchangeRequestController.handle(request);
    }
}

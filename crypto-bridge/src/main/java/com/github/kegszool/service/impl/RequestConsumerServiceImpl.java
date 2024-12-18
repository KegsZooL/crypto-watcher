package com.github.kegszool.service.impl;

import com.github.kegszool.service.RequestConsumerService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;

public class RequestConsumerServiceImpl implements RequestConsumerService {

    @Override
    @RabbitListener(queues = "${spring.rabbitmq.queues.request_queue}")
    public void consume(String message) {

    }
}

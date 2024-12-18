package com.github.kegszool.service.impl;

import com.github.kegszool.service.ResponseConsumerService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
public class ResponseConsumerServiceImpl implements ResponseConsumerService {

    @Override
    @RabbitListener(queues = "${spring.rabbitmq.queues.response_queue}")
    public void consume(String response) {

    }
}
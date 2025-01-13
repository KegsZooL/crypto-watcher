package com.github.kegszool.communication_service.impl;

import com.github.kegszool.DTO.DataTransferObject;
import com.github.kegszool.communication_service.RequestProducerService;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class RequestProducerServiceImpl implements RequestProducerService {

    @Value("${spring.rabbitmq.template.exchange}")
    private String EXCHANGE_NAME;
    private final RabbitTemplate rabbitTemplate;

    @Autowired
    public RequestProducerServiceImpl(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @Override
    public void produce(String routingKey, DataTransferObject dataTransferObject) {
        rabbitTemplate.convertAndSend(EXCHANGE_NAME, routingKey, dataTransferObject);
    }
}
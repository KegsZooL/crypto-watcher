package com.github.kegszool.communication_service.impl;

import com.github.kegszool.DTO.DataTransferObject;
import com.github.kegszool.communication_service.ResponseProducerService;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class ResponseProducerServiceImpl implements ResponseProducerService {

    private final RabbitTemplate rabbitTemplate;

    @Value("${spring.rabbitmq.template.exchange}")
    private String EXCHANGE_NAME;

    @Autowired
    public ResponseProducerServiceImpl(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @Override
    public void produce(DataTransferObject dataTransferObject, String routingKey) {
        rabbitTemplate.convertAndSend(EXCHANGE_NAME, routingKey, dataTransferObject);
    }
}
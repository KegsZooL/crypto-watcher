package com.github.kegszool.messaging.producer;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.amqp.AmqpException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import com.github.kegszool.messaging.dto.service.ServiceMessage;
import com.github.kegszool.messaging.service_message.ServiceMessageUtils;

@Service
public class RequestProducer implements RequestProducerService {

    @Value("${spring.rabbitmq.template.exchange}")
    private String EXCHANGE_NAME;

    private final RabbitTemplate rabbitTemplate;

    @Autowired
    public RequestProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @Override
    public void produce(String routingKey, ServiceMessage<?> serviceMessage) {
        if (ServiceMessageUtils.isDataValid(serviceMessage, routingKey)) {
            try {
                rabbitTemplate.convertAndSend(EXCHANGE_NAME, routingKey, serviceMessage);
                ServiceMessageUtils.logTransmittedMessage(serviceMessage, routingKey);
            } catch (AmqpException ex) {
               throw ServiceMessageUtils.handleAmqpException(routingKey, ex);
            }
        } else {
            throw ServiceMessageUtils.handleInvalidServiceMessage(serviceMessage, routingKey);
        }
    }
}
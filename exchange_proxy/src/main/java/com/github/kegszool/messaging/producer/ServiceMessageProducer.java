package com.github.kegszool.messaging.producer;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.annotation.Autowired;

import com.github.kegszool.utils.ServiceMessageUtils;
import com.github.kegszool.messaging.dto.service.ServiceMessage;

import org.springframework.amqp.AmqpException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

@Service
public class ServiceMessageProducer implements ProducerService {

    @Value("${spring.rabbitmq.template.exchange}")
    private String EXCHANGE_NAME;

    private final RabbitTemplate rabbitTemplate;

    @Autowired
    public ServiceMessageProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @Override
    public void produce(ServiceMessage<?> serviceMessage, String routingKey) {
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
package com.github.kegszool.messaging.producer;

import com.github.kegszool.messaging.dto.ServiceMessage;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

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
    public void produce(String routingKey, ServiceMessage serviceMessage) {
        rabbitTemplate.convertAndSend(EXCHANGE_NAME, routingKey, serviceMessage);
    }
}
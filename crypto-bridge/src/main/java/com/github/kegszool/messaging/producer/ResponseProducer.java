package com.github.kegszool.messaging.producer;

import com.github.kegszool.messaging.dto.ServiceMessage;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class ResponseProducer implements ResponseProducerService {

    private final RabbitTemplate rabbitTemplate;

    @Value("${spring.rabbitmq.template.exchange}")
    private String EXCHANGE_NAME;

    @Autowired
    public ResponseProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @Override
    public void produce(ServiceMessage serviceMessage, String routingKey) {
        rabbitTemplate.convertAndSend(EXCHANGE_NAME, routingKey, serviceMessage);
    }
}
package com.github.kegszool.communication_service.impl;

import com.github.kegszool.DTO.DataTransferObject;
import com.github.kegszool.communication_service.ResponseConsumerService;
import com.github.kegszool.controll.TelegramBotController;
import lombok.extern.log4j.Log4j2;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;

@Service
@Log4j2
public class ExchangeResponseConsumer implements ResponseConsumerService {

    private final TelegramBotController botController;

    @Autowired
    public ExchangeResponseConsumer(TelegramBotController botController) {
        this.botController = botController;
    }

    @Override
    @RabbitListener(queues = "${spring.rabbitmq.queues.response_from_exchange_queue}")
    public void consume(DataTransferObject dataTransferObject, @Header(AmqpHeaders.RECEIVED_ROUTING_KEY) String routingKey) {
       log.info("A response was received from the exchange: {}", dataTransferObject);
       botController.handleResponse(dataTransferObject, routingKey);
    }
}
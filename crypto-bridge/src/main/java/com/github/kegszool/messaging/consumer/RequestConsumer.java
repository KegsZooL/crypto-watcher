package com.github.kegszool.messaging.consumer;

import com.github.kegszool.messaging.dto.ServiceMessage;
import com.github.kegszool.controller.ExchangeRequestController;
import lombok.extern.log4j.Log4j2;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;

@Service
@Log4j2
public class RequestConsumer implements RequestConsumerService {

    private final ExchangeRequestController exchangeRequestController;

    @Autowired
    public RequestConsumer(ExchangeRequestController exchangeRequestController) {
        this.exchangeRequestController = exchangeRequestController;
    }

    @Override
    @RabbitListener(queues = "${spring.rabbitmq.queues.request_to_exchange_queue}")
    public void consume(ServiceMessage serviceMessage, @Header(AmqpHeaders.RECEIVED_ROUTING_KEY) String routingKey) {
        logReceivedRequest(serviceMessage, routingKey);
        exchangeRequestController.handle(serviceMessage, routingKey);
    }

    private void logReceivedRequest(ServiceMessage serviceMessage, String routingKey) {
        String request = serviceMessage.getData();
        String chatId = serviceMessage.getChatId();
        log.info("Request: \"{}\" for chat_id: \"{}\" has been received. Routing key: {}", request, chatId, routingKey);
    }
}
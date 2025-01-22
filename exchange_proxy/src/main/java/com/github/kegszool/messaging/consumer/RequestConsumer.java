package com.github.kegszool.messaging.consumer;

import com.github.kegszool.controller.RequestController;
import com.github.kegszool.messaging.dto.ServiceMessage;
//import com.github.kegszool.controller.RequestController;
import com.github.kegszool.utils.ServiceMessageUtils;
import lombok.extern.log4j.Log4j2;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;

@Service
@Log4j2
public class RequestConsumer implements RequestConsumerService {

    private final RequestController requestController;

    @Autowired
    public RequestConsumer(RequestController requestController) {
        this.requestController = requestController;
    }

    @Override
    @RabbitListener(queues = "${spring.rabbitmq.queues.request_to_exchange}")
    public void consume(ServiceMessage<String> serviceMessage, @Header(AmqpHeaders.RECEIVED_ROUTING_KEY) String routingKey) {
        if(ServiceMessageUtils.isDataValid(serviceMessage, routingKey)) {
            logReceivedRequest(serviceMessage, routingKey);
            requestController.handle(serviceMessage, routingKey);
        } else {
            throw ServiceMessageUtils.handleInvalidServiceMessage(serviceMessage, routingKey);
        }
    }

    private void logReceivedRequest(ServiceMessage<String> serviceMessage, String routingKey) {
        String data = serviceMessage.getData();
        String chatId = serviceMessage.getChatId();
        log.info("Request: \"{}\" for chat_id: \"{}\" has been received. Received data: {}", routingKey, chatId, data);
    }
}
package com.github.kegszool.messaging.consumer;

import com.github.kegszool.controller.RequestController;
import com.github.kegszool.messaging.dto.service.ServiceMessage;
import com.github.kegszool.utils.ServiceMessageUtils;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RequestConsumer implements RequestConsumerService {

    private final RequestController requestController;

    @Autowired
    public RequestConsumer(RequestController requestController) {
        this.requestController = requestController;
    }

    @Override
    @RabbitListener(queues = "${spring.rabbitmq.queues.request_to_database}")
    public void consume(ServiceMessage<?> serviceMessage, String routingKey) {
        if (ServiceMessageUtils.isDataValid(serviceMessage, routingKey)) {
            ServiceMessageUtils.logReceivedRequest(serviceMessage, routingKey);
            requestController.handle(serviceMessage, routingKey);
        } else {
            throw ServiceMessageUtils.handleInvalidServiceMessage(serviceMessage, routingKey);
        }
    }
}
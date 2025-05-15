package com.github.kegszool.messaging.consumer;

import org.springframework.stereotype.Service;
import com.fasterxml.jackson.core.type.TypeReference;
import org.springframework.beans.factory.annotation.Autowired;

import com.github.kegszool.bot.TelegramBotController;
import com.github.kegszool.messaging.dto.service.ServiceMessage;
import com.github.kegszool.messaging.service_message.ServiceMessageUtils;

@Service
public abstract class BaseResponseConsumer<T> implements ResponseConsumerService<T> {

    @Autowired
    protected TelegramBotController botController;
    protected abstract TypeReference<T> getTypeReference();

    @Override
    public void consume(ServiceMessage<T> serviceMessage, String routingKey) {
        if (ServiceMessageUtils.isDataValid(serviceMessage, routingKey)) {
            ServiceMessage<T> mappedMessage = ServiceMessageUtils.mapToServiceMessage(serviceMessage, getTypeReference());
            handleResponse(mappedMessage, routingKey);
        } else {
            throw ServiceMessageUtils.handleInvalidServiceMessage(serviceMessage, routingKey);
        }
    }

    protected abstract void logReceivedData(ServiceMessage<T> serviceMessage, String routingKey);

    private void handleResponse(ServiceMessage<T> mappedMessage, String routingKey) {
        logReceivedData(mappedMessage, routingKey);
        botController.handleResponse(mappedMessage, routingKey);
    }
}
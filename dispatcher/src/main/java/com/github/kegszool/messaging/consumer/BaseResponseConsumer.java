package com.github.kegszool.messaging.consumer;

import com.github.kegszool.bot.controll.TelegramBotController;
import com.github.kegszool.messaging.dto.service.ServiceMessage;
import com.github.kegszool.utils.ServiceMessageUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public abstract class BaseResponseConsumer<T> implements ResponseConsumerService<T> {

    @Autowired
    protected TelegramBotController botController;

    @Override
    public void consume(ServiceMessage<T> serviceMessage, String routingKey) {
        if (ServiceMessageUtils.isDataValid(serviceMessage, routingKey) && canHandle(routingKey)) {
            ServiceMessage<T> mappedMessage = ServiceMessageUtils.mapToServiceMessage(serviceMessage, getDataClass());
            handleResponse(mappedMessage, routingKey);
        } else {
            throw ServiceMessageUtils.handleInvalidServiceMessage(serviceMessage, routingKey);
        }
    }
    protected abstract boolean canHandle(String routingKey);
    protected abstract Class<T> getDataClass();
    protected abstract void logReceivedData(ServiceMessage<T> serviceMessage, String routingKey);

    private void handleResponse(ServiceMessage<T> mappedMessage, String routingKey) {
        logReceivedData(mappedMessage, routingKey);
        botController.handleResponse(mappedMessage, routingKey);
    }
}
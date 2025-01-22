package com.github.kegszool.messaging.consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.kegszool.bot.controll.TelegramBotController;
import com.github.kegszool.exception.messaging.conversion.DataConversionException;
import com.github.kegszool.messaging.dto.ServiceMessage;
import com.github.kegszool.utils.ServiceMessageUtils;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

@Component
@Log4j2
public abstract class BaseResponseConsumer<T> implements ResponseConsumerService<T> {

    protected final TelegramBotController botController;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public BaseResponseConsumer(TelegramBotController botController) {
        this.botController = botController;
    }

    @Override
    public void consume(ServiceMessage<T> serviceMessage, String routingKey) {
        if(ServiceMessageUtils.isDataValid(serviceMessage, routingKey) && canHandle(routingKey)) {
            ServiceMessage<T> mappedMessage = mapToObject(serviceMessage);
            handleResponse(mappedMessage, routingKey);
        } else {
            throw ServiceMessageUtils.handleInvalidServiceMessage(serviceMessage, routingKey);
        }
    }

    protected abstract boolean canHandle(String routingKey);

    private ServiceMessage<T> mapToObject(ServiceMessage<?> serviceMessage) {
        Object serviceMessageData = serviceMessage.getData();
        try {
            T data = objectMapper.convertValue(serviceMessageData, getDataClass());

            ServiceMessage<T> mappedMessage = new ServiceMessage<>();
            mappedMessage.setChatId(serviceMessage.getChatId());
            mappedMessage.setData(data);

            return mappedMessage;
        }
        catch (IllegalArgumentException ex) {
            throw handleConversionError(serviceMessageData);
        }
    }

    protected abstract Class<T> getDataClass();

    private DataConversionException handleConversionError(Object serviceMessageData) {
        Class<?> dataClass = serviceMessageData.getClass();
        log.error("Error converting object of type \"{}\" to type \"{}\".",
                dataClass, getDataClass());
        return new DataConversionException("Failed to convert service message data.");
    }

    private void handleResponse(ServiceMessage<T> mappedMessage, String routingKey) {
        logReceivedData(mappedMessage, routingKey);
        botController.handleResponse(mappedMessage, routingKey);
    }

    protected abstract void logReceivedData(ServiceMessage<T> serviceMessage, String routingKey);
}
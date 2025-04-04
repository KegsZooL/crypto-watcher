package com.github.kegszool.utils;

import com.github.kegszool.messaging.dto.service.ServiceMessage;
import com.github.kegszool.exception.messaging.conversion.DataConversionException;
import com.github.kegszool.exception.messaging.service_message.InvalidServiceMessageException;
import com.github.kegszool.exception.messaging.service_message.ServiceMessageSendingException;

import org.springframework.amqp.AmqpException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.log4j.Log4j2;

@Log4j2
public class ServiceMessageUtils {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    public static boolean isDataValid(ServiceMessage<?> serviceMessage, String routingKey) {
        String chatId = serviceMessage.getChatId();
        Integer messageId = serviceMessage.getMessageId();
        return serviceMessage != null && serviceMessage.getData() != null
                && routingKey != null && !routingKey.isEmpty()
                && chatId != null && !chatId.isEmpty()
                && messageId != null;
    }

    public static InvalidServiceMessageException handleInvalidServiceMessage(
            ServiceMessage<?> serviceMessage, String routingKey
    ) {
        if (serviceMessage == null) {
            String errorMsg = "Service message is null!";
            log.error("errorMsg");
            return new InvalidServiceMessageException(errorMsg);
        }
        return createInvalidServiceMessageException(serviceMessage, routingKey);
    }

    private static InvalidServiceMessageException createInvalidServiceMessageException(
            ServiceMessage<?> serviceMessage, String routingKey
    ) {
        String serviceMessageData = fetchServiceMessageData(serviceMessage, routingKey);
        log.error("Received invalid service message. {}", serviceMessageData);
        return new InvalidServiceMessageException(serviceMessageData);
    }

    private static String fetchServiceMessageData(ServiceMessage<?> serviceMessage, String routinKey) {
        return String.format("Routing key: \"%s\", Data: \"%s\", ChatId: \"%s\", MessageId: \"%s\"",
                routinKey, serviceMessage.getData().toString(),
                serviceMessage.getChatId(), serviceMessage.getMessageId().toString());
    }

    public static ServiceMessageSendingException handleAmqpException(String routingKey, AmqpException ex) {
        log.error("Failed to send service message with routing key \"{}\". Exception: {}",
                routingKey, ex.getMessage(), ex);
        return new ServiceMessageSendingException(String.format("Routing key: \"%s\"", routingKey), ex);
    }

    public static void logTransmittedMessage(ServiceMessage<?> serviceMessage, String routingKey) {
        String classOfDataInServiceMessage = serviceMessage.getData().getClass().getSimpleName();
        log.info("Service message containing data of type \"{}\" sent with routing key \"{}\"",
                classOfDataInServiceMessage, routingKey);
    }

    public static <T> ServiceMessage<T> mapToServiceMessage(ServiceMessage<?> serviceMessage, Class<T> targetClass) {
        Object serviceMessageData = serviceMessage.getData();
        try {
            T mappedData = OBJECT_MAPPER.convertValue(serviceMessageData, targetClass);
            return new ServiceMessage<>(
                    serviceMessage.getMessageId(), serviceMessage.getChatId(), mappedData
            );
        } catch (IllegalArgumentException ex) {
            throw handleConversionError(serviceMessageData, targetClass);
        }
    }

    public static <T> DataConversionException handleConversionError(Object serviceMessageData, Class<T> targetClass) {
        Class<?> dataClass = serviceMessageData.getClass();
        log.error("Error converting object of type \"{}\" to type \"{}\".",
                dataClass, targetClass);
        return new DataConversionException("Failed to convert service message data.");
    }
}
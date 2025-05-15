package com.github.kegszool.messaging.service_message;

import lombok.extern.log4j.Log4j2;
import org.springframework.amqp.AmqpException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;

import com.github.kegszool.messaging.dto.service.ServiceMessage;
import com.github.kegszool.messaging.service_message.exception.InvalidServiceMessageException;
import com.github.kegszool.messaging.service_message.exception.ServiceMessageSendingException;
import com.github.kegszool.messaging.service_message.exception.InvalidServiceMessagePayloadException;

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

    private static String fetchServiceMessageData(ServiceMessage<?> serviceMessage, String routingKey) {
        return String.format("Routing key: \"%s\", Data: \"%s\", ChatId: \"%s\", MessageId: \"%s\"",
                routingKey, serviceMessage.getData().toString(),
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

    public static <T> ServiceMessage<T> mapToServiceMessage(ServiceMessage<?> serviceMessage, TypeReference<T> typeReference) {
        Object serviceMessageData = serviceMessage.getData();
        try {
            T mappedData = OBJECT_MAPPER.convertValue(serviceMessageData, typeReference);
            return new ServiceMessage<>(
                    serviceMessage.getMessageId(), serviceMessage.getChatId(), mappedData
            );
        } catch (IllegalArgumentException ex) {
            throw handleConversionError(serviceMessageData, typeReference.getClass());
        }
    }

    public static <T> InvalidServiceMessagePayloadException handleConversionError(Object serviceMessageData, Class<T> targetClass) {
        Class<?> dataClass = serviceMessageData.getClass();
        log.error("Error converting object of type \"{}\" to type \"{}\".",
                dataClass, targetClass);
        return new InvalidServiceMessagePayloadException("Failed to convert service message data.");
    }
}
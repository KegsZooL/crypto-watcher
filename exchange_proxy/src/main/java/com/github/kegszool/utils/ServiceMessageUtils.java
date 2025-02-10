package com.github.kegszool.utils;

import com.github.kegszool.exception.service_message.InvalidServiceMessageException;
import com.github.kegszool.exception.service_message.ServiceMessageSendingException;
import com.github.kegszool.messaging.dto.service.ServiceMessage;
import lombok.extern.log4j.Log4j2;
import org.springframework.amqp.AmqpException;

import java.security.KeyPair;

@Log4j2
public class ServiceMessageUtils {

    public static boolean isDataValid(ServiceMessage<?> serviceMessage, String routingKey) {
        String chatId = serviceMessage.getChatId();
        return serviceMessage != null && serviceMessage.getData() != null
                && routingKey != null && !routingKey.isEmpty()
                && chatId != null && !chatId.isEmpty();
    }

    public static void logReceivedRequest(ServiceMessage<?> serviceMessage, String routingKey) {
        String data = serviceMessage.getData().toString();
        String chatId = serviceMessage.getChatId();
        log.info("Request: \"{}\" for chat_id: \"{}\" has been received. Received data: {}", routingKey, chatId, data);
    }

    public static InvalidServiceMessageException handleInvalidServiceMessage(
            ServiceMessage<?> serviceMessage, String routingKey
    ) {
        Object data = serviceMessage != null ? serviceMessage.getData() : "null";
        String chatId = serviceMessage != null ? serviceMessage.getChatId() : "null";

        log.error("Received invalid service message. " +
                "Routing key: \"{}\", Data: \"{}\", ChatId: \"{}\"", routingKey, data, chatId);
        return new InvalidServiceMessageException(String.format(
                "Routing key: \"%s\". Data: \"%s\", ChatId: \"%s\"", routingKey, data, chatId));
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
}
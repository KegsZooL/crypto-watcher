package com.github.kegszool.utils;

import com.github.kegszool.exception.messaging.service_message.InvalidServiceMessageException;
import com.github.kegszool.exception.messaging.service_message.ServiceMessageSendingException;
import com.github.kegszool.messaging.dto.service.ServiceMessage;
import lombok.extern.log4j.Log4j2;
import org.springframework.amqp.AmqpException;

@Log4j2
public class ServiceMessageUtils {

    public static boolean isDataValid(ServiceMessage<?> serviceMessage, String routingKey) {
        String chatId = serviceMessage.getChatId();
        Integer messageId = serviceMessage.getMessageId();
        return serviceMessage != null && serviceMessage.getData() != null
                && routingKey != null && !routingKey.isEmpty()
                && chatId != null && !chatId.isEmpty()
                && messageId != null;
    }

    public static void logReceivedRequest(ServiceMessage<?> serviceMessage, String routingKey) {
        String data = serviceMessage.getData().toString();
        String chatId = serviceMessage.getChatId();
        log.info("Request: \"{}\" for chat_id: \"{}\" has been received. Received data: {}", routingKey, chatId, data);
    }

    public static InvalidServiceMessageException handleInvalidServiceMessage(
            ServiceMessage<?> serviceMessage, String routingKey
    ) {
        if(serviceMessage == null) {
            String errorMsg = "Service message is null!";
            log.error("errorMsg");
            return new InvalidServiceMessageException(errorMsg);
        }
        return createInvalidServiceMessageException(serviceMessage, routingKey);
    }

    private static InvalidServiceMessageException createInvalidServiceMessageException(
            ServiceMessage<?> serviceMessage, String routingKey
    ) {
        String data, chatId, messageId;
        data = serviceMessage.getData().toString();
        chatId = serviceMessage.getChatId();
        messageId = serviceMessage.getMessageId().toString();

        String pattern = "Routing key: \"{}\", Data: \"{}\", ChatId: \"{}\", MessageId: \"{}\"";

        log.error("Received invalid service message. "
                + pattern, routingKey, data, chatId, messageId);
        return new InvalidServiceMessageException(
                String.format(pattern, routingKey, data, chatId, messageId));
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
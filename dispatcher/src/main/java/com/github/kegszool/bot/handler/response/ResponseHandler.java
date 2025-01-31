package com.github.kegszool.bot.handler.response;

import com.github.kegszool.messaging.dto.service.ServiceMessage;
import org.telegram.telegrambots.meta.api.methods.botapimethods.PartialBotApiMethod;

public interface ResponseHandler {
    boolean canHandle(String routingKey);
    PartialBotApiMethod<?> handle(ServiceMessage<?> serviceMessage);
}

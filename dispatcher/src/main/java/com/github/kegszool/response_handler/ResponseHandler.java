package com.github.kegszool.response_handler;

import org.telegram.telegrambots.meta.api.methods.botapimethods.PartialBotApiMethod;

public interface ResponseHandler {
    boolean canHandle(String routingKey);
    PartialBotApiMethod<?> handle(String response);
}

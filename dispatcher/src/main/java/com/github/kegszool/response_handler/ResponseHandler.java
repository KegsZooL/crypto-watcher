package com.github.kegszool.response_handler;

import com.github.kegszool.DTO.DataTransferObject;
import org.telegram.telegrambots.meta.api.methods.botapimethods.PartialBotApiMethod;

public interface ResponseHandler {
    boolean canHandle(String routingKey);
    PartialBotApiMethod<?> handle(DataTransferObject dataTransferObject);
}

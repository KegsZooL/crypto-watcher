package com.github.kegszool.controll;

import com.github.kegszool.DTO.DataTransferObject;
import com.github.kegszool.exception.ResponseHandlerNotFoundException;
import com.github.kegszool.response_handler.ResponseHandler;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.botapimethods.PartialBotApiMethod;

import java.util.List;

@Component
@Log4j2
public class ResponseRouter {

    private final List<ResponseHandler> responseHandlers;

    @Autowired
    public ResponseRouter(List<ResponseHandler> responseHandlers) {
        this.responseHandlers = responseHandlers;
    }

    public PartialBotApiMethod<?> routeAndHandle(DataTransferObject dataTransferObject, String routingKey) {
        return responseHandlers.stream()
                .filter(responseHandler -> responseHandler.canHandle(routingKey))
                .findFirst()
                .map(responseHandler -> responseHandler.handle(dataTransferObject))
                .orElseThrow(() -> processMissingResponseHandler(dataTransferObject, routingKey));
    }

    private ResponseHandlerNotFoundException processMissingResponseHandler(DataTransferObject dataTransferObject, String routingKey) {
        String warnMessage = String.format(
                "No response handler found for routing key: %s / ChatId: {}",
                routingKey, dataTransferObject.getChatId()
        );
        log.warn(warnMessage);
        throw new ResponseHandlerNotFoundException(warnMessage);
    }
}
package com.github.kegszool.controll;

import com.github.kegszool.response_handler.ResponseHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.botapimethods.PartialBotApiMethod;

import java.util.List;

@Component
public class ResponseRouter {

    private final List<ResponseHandler> responseHandlers;

    @Autowired
    public ResponseRouter(List<ResponseHandler> responseHandlers) {
        this.responseHandlers = responseHandlers;
    }

    public PartialBotApiMethod<?> routeAndHandle(String response, String routingKey) {
        return null;
    }
}

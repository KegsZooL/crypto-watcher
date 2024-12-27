package com.github.kegszool.controll;

import com.github.kegszool.exception.UnknowRequestException;
import com.github.kegszool.menu.callback_handler.CallbackHandler;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.botapimethods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

import java.util.List;

@Component
@Log4j2
public class CallbackQueryRouter {

    private final List<CallbackHandler> callbackHandlers;

    @Autowired
    public CallbackQueryRouter(List<CallbackHandler> callbackHandlers) {
        this.callbackHandlers = callbackHandlers;
    }

    public PartialBotApiMethod<?> routeAndHandle(CallbackQuery query) {
        String request = query.getData();
        return callbackHandlers.stream()
                .filter(callbackHandler -> callbackHandler.canHandle(request))
                .findFirst()
                .map(callbackHandler -> callbackHandler.handle(query))
                .orElseThrow(() -> handleUnknowRequestException(query));
    }

    private UnknowRequestException handleUnknowRequestException(CallbackQuery query) {
        String request = query.getData();
        Long chatId = query.getMessage().getChatId();
        String errorMessage = String.format("Unknown request detected: %s. Chat id: %d", request, chatId);
        log.error(errorMessage);
        throw new UnknowRequestException(errorMessage);
    }
}
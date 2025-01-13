package com.github.kegszool.controll;

import com.github.kegszool.exception.UpdateHandlerNotFoundException;
import com.github.kegszool.menu.UpdateHandler;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.botapimethods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;

@Component
@Log4j2
public class UpdateRouter {

    private final List<UpdateHandler> handlers;

    @Autowired
    public UpdateRouter(List<UpdateHandler> handlers) {
        this.handlers = handlers;
    }

    public PartialBotApiMethod<?> routeAndHandle(Update update) {
        return handlers.stream()
                .filter(handler -> handler.canHandle(update))
                .findFirst()
                .map(handler -> handler.handle(update))
                .orElseThrow(() -> processMissingUpdateHandler(update));
    }

    private UpdateHandlerNotFoundException processMissingUpdateHandler(Update update) {
        Long chatId = update.getMessage().getChatId();
        String warnMessage = "No handler was found for the update. ChatId: " + chatId;
        log.warn(warnMessage);
        throw new UpdateHandlerNotFoundException(warnMessage);
    }
}
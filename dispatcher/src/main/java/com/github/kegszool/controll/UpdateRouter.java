package com.github.kegszool.controll;

import com.github.kegszool.menu.UpdateHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.botapimethods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;

@Component
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
                .orElseThrow(() -> new RuntimeException()); //TODO: сделать кастомное исключение
    }
}

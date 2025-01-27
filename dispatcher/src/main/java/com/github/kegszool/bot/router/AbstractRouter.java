package com.github.kegszool.bot.router;

import com.github.kegszool.exception.bot.handler.HandlerNotFoundException;
import org.telegram.telegrambots.meta.api.methods.botapimethods.PartialBotApiMethod;

import java.util.List;

public abstract class AbstractRouter<T, H> {

    private final List<H> handlers;

    protected AbstractRouter(List<H> handlers) {
        this.handlers = handlers;
    }

    public PartialBotApiMethod<?> routeAndHandle(T data, Object key) {
        return handlers.stream()
                .filter(handler -> canHandle(handler, key))
                .findFirst()
                .map(handler -> handle(handler, data))
                .orElseThrow(() -> proccessMissingHandler(data, key));
    }

    protected abstract boolean canHandle(H handler, Object key);

    protected abstract PartialBotApiMethod<?> handle(H handler, T data);

    protected abstract HandlerNotFoundException proccessMissingHandler(T input, Object key);
}
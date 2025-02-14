package com.github.kegszool.bot.router;

import com.github.kegszool.bot.handler.result.HandlerResult;
import com.github.kegszool.exception.bot.handler.HandlerNotFoundException;

import java.util.List;

public abstract class AbstractRouter<T, H, K> {

    private final List<H> handlers;

    protected AbstractRouter(List<H> handlers) {
        this.handlers = handlers;
    }

    public HandlerResult routeAndHandle(T data, K key) {
        return handlers.stream()
                .filter(handler -> canHandle(handler, key))
                .findFirst()
                .map(handler -> handle(handler, data))
                .orElseThrow(() -> proccessMissingHandler(data, key));
    }

    protected abstract boolean canHandle(H handler, K key);

    protected abstract HandlerResult handle(H handler, T data);

    protected abstract HandlerNotFoundException proccessMissingHandler(T input, K key);
}
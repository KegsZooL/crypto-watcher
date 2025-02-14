package com.github.kegszool.bot.menu.command;

import com.github.kegszool.bot.handler.UpdateHandler;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.methods.botapimethods.PartialBotApiMethod;

public abstract class Command<T> implements UpdateHandler {

    @Override
    public boolean canHandle(Update update) {
        return isApplicable(update) && canHandleCommand(extractCommand(update));
    }

    @Override
    public PartialBotApiMethod<?> handle(Update update) {
        T data = extractData(update);
        return handleCommand(data);
    }

    protected abstract T extractData(Update update);

    protected abstract String extractCommand(Update update);

    protected abstract boolean isApplicable(Update update);

    protected abstract boolean canHandleCommand(String command);

    protected abstract PartialBotApiMethod<?> handleCommand(T entity);
}
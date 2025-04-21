package com.github.kegszool.command;

import com.github.kegszool.messaging.util.MessageUtils;
import com.github.kegszool.update.UpdateHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.methods.botapimethods.PartialBotApiMethod;

@Component
public abstract class Command<T> implements UpdateHandler {

    @Autowired
    protected MessageUtils messageUtils;

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
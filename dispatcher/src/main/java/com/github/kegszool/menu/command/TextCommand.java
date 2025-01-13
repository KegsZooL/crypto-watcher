package com.github.kegszool.menu.command;

import com.github.kegszool.menu.UpdateHandler;
import org.telegram.telegrambots.meta.api.methods.botapimethods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;

public abstract class TextCommand implements UpdateHandler {

    @Override
    public boolean canHandle(Update update) {
        if(update.hasMessage() && update.getMessage().hasText()) {
            return canHandleCommand(update.getMessage().getText());
        }
        return false;
    }

    @Override
    public PartialBotApiMethod<?> handle(Update update) {
        return handleCommand(update);
    }

    protected abstract boolean canHandleCommand(String command);
    protected abstract PartialBotApiMethod<?> handleCommand(Update update);
}

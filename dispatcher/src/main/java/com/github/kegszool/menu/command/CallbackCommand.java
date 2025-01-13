package com.github.kegszool.menu.command;

import com.github.kegszool.menu.UpdateHandler;
import org.telegram.telegrambots.meta.api.methods.botapimethods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Update;

public abstract class CallbackCommand implements UpdateHandler {

    @Override
    public boolean canHandle(Update update) {
        return update.hasCallbackQuery() && canHandleCommand(update.getCallbackQuery().getData());
    }

    @Override
    public PartialBotApiMethod<?> handle(Update update) {
        return handleCommand(update.getCallbackQuery());
    }

    protected abstract boolean canHandleCommand(String command);
    protected abstract PartialBotApiMethod<?> handleCommand(CallbackQuery query);
}

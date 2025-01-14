package com.github.kegszool.bot.menu.command.callback;

import com.github.kegszool.bot.menu.command.Command;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Update;

public abstract class CallbackCommand extends Command<CallbackQuery> {

    @Override
    protected boolean isApplicable(Update update) {
        return update.hasCallbackQuery();
    }

    @Override
    protected String extractCommand(Update update) {
        return update.getCallbackQuery().getData();
    }

    @Override
    protected CallbackQuery extractData(Update update) {
        return update.getCallbackQuery();
    }
}

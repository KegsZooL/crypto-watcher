package com.github.kegszool.command;

import org.telegram.telegrambots.meta.api.objects.Update;

public abstract class TextCommand extends Command<Update> {

    @Override
    protected boolean isApplicable(Update update) {
       return update.hasMessage() && update.getMessage().hasText();
    }

    @Override
    protected String extractCommand(Update update) {
        return update.getMessage().getText();
    }

    @Override
    protected Update extractData(Update update) {
        return update;
    }
}

package com.github.kegszool.language.command;

import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Value;

import com.github.kegszool.language.service.MenuLanguageChanger;
import com.github.kegszool.command.callback.CallbackCommand;

import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.methods.botapimethods.PartialBotApiMethod;

@Component
public class ChangeLanguageCommand extends CallbackCommand {

    private final String languagePrefix;
    private final MenuLanguageChanger changer;

    public ChangeLanguageCommand(
           @Value("${menu.language_change.prefix}") String languagePrefix,
           MenuLanguageChanger changer
    ) {
        this.languagePrefix = languagePrefix;
        this.changer = changer;
    }

    @Override
    protected boolean canHandleCommand(String command) {
        return command.startsWith(languagePrefix);
    }

    @Override
    protected PartialBotApiMethod<?> handleCommand(CallbackQuery callbackQuery) {
        return changer.change(callbackQuery);
    }
}
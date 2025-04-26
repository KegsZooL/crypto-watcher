package com.github.kegszool.command.callback.impl;

import com.github.kegszool.command.callback.CallbackCommand;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.botapimethods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

@Component
public class NoOpCommand extends CallbackCommand {

    private final String noOpPrefix;

    public NoOpCommand(
            @Value("${menu.action.noop_prefix}") String noOpPrefix
    ) {
        this.noOpPrefix = noOpPrefix;
    }

    @Override
    protected boolean canHandleCommand(String command) {
        return command.startsWith(noOpPrefix);
    }

    @Override
    protected PartialBotApiMethod<?> handleCommand(CallbackQuery entity) {
        return null;
    }
}
package com.github.kegszool.settings;

import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Value;

import com.github.kegszool.messaging.util.MessageUtils;
import com.github.kegszool.command.callback.CallbackCommand;

import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.methods.botapimethods.PartialBotApiMethod;

@Component
public class DisplaySettingsMenu extends CallbackCommand {

    private final String menuName;
    private final MessageUtils messageUtils;

    public DisplaySettingsMenu(
            @Value("${menu.settings.name}") String menuName,
            MessageUtils messageUtils
    ) {
        this.menuName = menuName;
        this.messageUtils = messageUtils;
    }

    @Override
    protected boolean canHandleCommand(String command) {
        return menuName.equals(command);
    }

    @Override
    protected PartialBotApiMethod<?> handleCommand(CallbackQuery callbackQuery) {
        return messageUtils.recordAndCreateEditMessageByMenuName(callbackQuery, menuName);
    }
}
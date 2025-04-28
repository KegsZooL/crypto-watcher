package com.github.kegszool.settings;

import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Value;

import com.github.kegszool.messaging.util.MessageUtils;
import com.github.kegszool.command.callback.CallbackCommand;

import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.methods.botapimethods.PartialBotApiMethod;

@Component
public class DisplaySettingsMenu extends CallbackCommand {

    private final String command;
    private final String menuName;
    private final MessageUtils messageUtils;

    public DisplaySettingsMenu(
            @Value("${bot.command.display_settings_menu}") String command,
            @Value("${menu.settings.name}") String menuName,
            MessageUtils messageUtils
    ) {
        this.command = command;
        this.menuName = menuName;
        this.messageUtils = messageUtils;
    }

    @Override
    protected boolean canHandleCommand(String command) {
        return this.command.equals(command) || menuName.equals(command);
    }

    @Override
    protected PartialBotApiMethod<?> handleCommand(CallbackQuery callbackQuery) {
        String chatId = messageUtils.extractChatId(callbackQuery);
        Integer messageId = callbackQuery.getMessage().getMessageId();
        return messageUtils.recordAndCreateEditMessageByMenuName(chatId, messageId, menuName);
    }
}
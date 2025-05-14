package com.github.kegszool.notification.creation.command;

import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.annotation.Autowired;

import com.github.kegszool.messaging.util.MessageUtils;
import com.github.kegszool.command.callback.CallbackCommand;

import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.methods.botapimethods.PartialBotApiMethod;

@Component
public class DisplayCreationNotificationMenuCallbackCommand extends CallbackCommand {

    private final String menuName;
    private final MessageUtils messageUtils;

    @Autowired
    public DisplayCreationNotificationMenuCallbackCommand(
            @Value("${menu.notification_creation.name}") String menuName,
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
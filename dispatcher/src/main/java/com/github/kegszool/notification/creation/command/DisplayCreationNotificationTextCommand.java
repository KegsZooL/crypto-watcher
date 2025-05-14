package com.github.kegszool.notification.creation.command;

import com.github.kegszool.command.TextCommand;
import com.github.kegszool.messaging.util.MessageUtils;

import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.annotation.Autowired;

import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.methods.botapimethods.PartialBotApiMethod;

@Component
public class DisplayCreationNotificationTextCommand extends TextCommand {

    private final String command;
    private final String menuName;
    private final MessageUtils messageUtils;

    @Autowired
    public DisplayCreationNotificationTextCommand(
            @Value("${menu.notification_creation.display_menu_command}") String command,
            @Value("${menu.notification_creation.name}") String menuName,
            MessageUtils messageUtils
    ) {
        this.command = command;
        this.menuName = menuName;
        this.messageUtils = messageUtils;
    }

    @Override
    protected boolean canHandleCommand(String command) {
        return this.command.equals(command);
    }

    @Override
    protected PartialBotApiMethod<?> handleCommand(Update entity) {
        return messageUtils.applyMenuSequenceAndCreateMessage(entity, menuName);
    }
}
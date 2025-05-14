package com.github.kegszool.notification.deletion.command;

import com.github.kegszool.command.TextCommand;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.annotation.Autowired;

import com.github.kegszool.messaging.util.MessageUtils;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.methods.botapimethods.PartialBotApiMethod;

@Component
public class DisplayDeletionNotificationMenuTextCommand extends TextCommand {

    private final String menuName;
    private final String command;
    private final MessageUtils messageUtils;

    @Autowired
    public DisplayDeletionNotificationMenuTextCommand(
            @Value("${menu.notification_deletion.name}") String menuName,
            @Value("${menu.notification_deletion.command}") String command,
            MessageUtils messageUtils
    ) {
        this.menuName = menuName;
        this.command = command;
        this.messageUtils = messageUtils;
    }

    @Override
    protected boolean canHandleCommand(String command) {
        return this.command.equals(command);
    }

    @Override
    protected PartialBotApiMethod<?> handleCommand(Update update) {
        return messageUtils.applyMenuSequenceAndCreateMessage(update, menuName);
    }
}
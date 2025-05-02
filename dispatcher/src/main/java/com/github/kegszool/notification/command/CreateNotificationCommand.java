package com.github.kegszool.notification.command;

import com.github.kegszool.command.text.TextCommand;
import com.github.kegszool.notification.NotificationHandler;

import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Value;

import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.methods.botapimethods.PartialBotApiMethod;

@Component
public class CreateNotificationCommand extends TextCommand {

    private final String prefix;
    private final NotificationHandler notificationHandler;

    public CreateNotificationCommand(
            @Value("${bot.command.create_notification}") String prefix,
            NotificationHandler notificationHandler
    ) {
        this.prefix = prefix;
        this.notificationHandler = notificationHandler;
    }

    @Override
    protected boolean canHandleCommand(String command) {
        return command.startsWith(prefix);
    }

    @Override
    protected PartialBotApiMethod<?> handleCommand(Update update) {
        return notificationHandler.createByFullCommand(update.getMessage());
    }
}
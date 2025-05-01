package com.github.kegszool.notification.command;

import com.github.kegszool.command.text.TextCommand;
import com.github.kegszool.notification.NotificationHandler;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.botapimethods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
public class CreateNotificationCommand extends TextCommand {

    private final String command;
    private final NotificationHandler notificationHandler;

    public CreateNotificationCommand(
            @Value("${bot.command.create_notification}") String command,
            NotificationHandler notificationHandler
    ) {
        this.command = command;
        this.notificationHandler = notificationHandler;
    }

    @Override
    protected boolean canHandleCommand(String command) {
        return this.command.equals(command);
    }

    @Override
    protected PartialBotApiMethod<?> handleCommand(Update update) {
        return notificationHandler.createByFullCommand(update.getMessage());
    }
}
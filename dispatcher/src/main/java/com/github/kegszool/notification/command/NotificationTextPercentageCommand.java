package com.github.kegszool.notification.command;

import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;

import com.github.kegszool.command.TextCommand;
import com.github.kegszool.notification.NotificationHandler;

import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.methods.botapimethods.PartialBotApiMethod;

@Component
public class NotificationTextPercentageCommand extends TextCommand {

    private final static String COMMAND_PATTERN = "^[+-]?\\d+(\\.\\d+)?%$";
    private final NotificationHandler notificationHandler;

    @Autowired
    public NotificationTextPercentageCommand(
            NotificationHandler notificationHandler
    ) {
        this.notificationHandler = notificationHandler;
    }

    @Override
    protected boolean canHandleCommand(String command) {
        return command.matches(COMMAND_PATTERN);
    }

    @Override
    protected PartialBotApiMethod<?> handleCommand(Update update) {
        return notificationHandler.createAndSendByPercentageInput(update.getMessage());
    }
}
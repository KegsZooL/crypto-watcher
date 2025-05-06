package com.github.kegszool.notification.deletion.command;

import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Value;

import com.github.kegszool.command.callback.CallbackCommand;
import com.github.kegszool.notification.deletion.NotificationDeletionHandler;

import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.methods.botapimethods.PartialBotApiMethod;

@Component
public class DeleteNotificationCommand extends CallbackCommand {

    private final String callbackPrefix;
    private final NotificationDeletionHandler deletionHandler;

    public DeleteNotificationCommand(
            @Value("${menu.notification_deletion.prefix}") String callbackPrefix,
            NotificationDeletionHandler deletionHandler
    ) {
        this.callbackPrefix = callbackPrefix;
        this.deletionHandler = deletionHandler;
    }

    @Override
    protected boolean canHandleCommand(String command) {
        return command.startsWith(callbackPrefix);
    }

    @Override
    protected PartialBotApiMethod<?> handleCommand(CallbackQuery callbackQuery) {
        return deletionHandler.deleteAndCreateAnswerUpdateMsg(callbackQuery, callbackPrefix);
    }
}
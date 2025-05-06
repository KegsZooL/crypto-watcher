package com.github.kegszool.notification.selection;

import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.annotation.Autowired;

import com.github.kegszool.messaging.util.MessageUtils;
import com.github.kegszool.command.callback.CallbackCommand;
import com.github.kegszool.notification.NotificationContextBuffer;

import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.methods.botapimethods.PartialBotApiMethod;

@Component
public class DisplaySelectCoinNotificationMenu extends CallbackCommand {

    private final String menuName;
    private final String oneTimeNotificationCommand;
    private final String recurringNotificationCommand;

    private final NotificationContextBuffer notificationContextBuffer;
    private final MessageUtils messageUtils;

    private boolean isRecurring = false;

    @Autowired
    public DisplaySelectCoinNotificationMenu(
            @Value("${menu.select_coin_notification.name}") String menuName,
            @Value("${menu.action.create_one_time_notification}") String oneTimeNotificationCommand,
            @Value("${menu.action.create_recurring_notification}") String recurringNotificationCommand,
            MessageUtils messageUtils,
            NotificationContextBuffer notificationContextBuffer
    ) {
        this.menuName = menuName;
        this.oneTimeNotificationCommand = oneTimeNotificationCommand;
        this.recurringNotificationCommand = recurringNotificationCommand;
        this.messageUtils = messageUtils;
        this.notificationContextBuffer = notificationContextBuffer;
    }

    @Override
    protected boolean canHandleCommand(String command) {
        if (oneTimeNotificationCommand.equals(command)) {
            isRecurring = false;
            return true;
        } else if (recurringNotificationCommand.equals(command)) {
            return isRecurring = true;
        }
        return false;
    }

    @Override
    protected PartialBotApiMethod<?> handleCommand(CallbackQuery callbackQuery) {
        Long chatId = callbackQuery.getMessage().getChatId();
        notificationContextBuffer.setType(chatId, isRecurring);
        return messageUtils.recordAndCreateEditMessageByMenuName(callbackQuery, menuName);
    }
}
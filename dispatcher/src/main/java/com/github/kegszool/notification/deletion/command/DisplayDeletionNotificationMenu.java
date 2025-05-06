package com.github.kegszool.notification.deletion.command;

import com.github.kegszool.command.callback.CallbackCommand;
import com.github.kegszool.messaging.util.MessageUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.botapimethods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

@Component
public class DisplayDeletionNotificationMenu extends CallbackCommand {

    private final String menuName;
    private final MessageUtils messageUtils;

    @Autowired
    public DisplayDeletionNotificationMenu(
            @Value("${menu.notification_deletion.name}") String menuName,
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

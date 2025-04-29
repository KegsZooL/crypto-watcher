package com.github.kegszool.notification.command;

import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.github.kegszool.messaging.util.MessageUtils;
import com.github.kegszool.command.callback.CallbackCommand;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.methods.botapimethods.PartialBotApiMethod;

@Component
public class DisplayNotificationMenu extends CallbackCommand {

    private final String menuName;
    private final MessageUtils messageUtils;

    @Autowired
    public DisplayNotificationMenu(
            @Value("${menu.notification.name}") String menuName,
            MessageUtils messageUtils
    ) {
        this.menuName = menuName;
        this.messageUtils = messageUtils;
    }

    @Override
    protected boolean canHandleCommand(String command) {
        return menuName.startsWith(command);
    }

    @Override
    protected PartialBotApiMethod<?> handleCommand(CallbackQuery callbackQuery) {
        String chatId = messageUtils.extractChatId(callbackQuery);
        Integer messageId = callbackQuery.getMessage().getMessageId();
        return messageUtils.recordAndCreateEditMessageByMenuName(chatId, messageId, menuName);
    }
}
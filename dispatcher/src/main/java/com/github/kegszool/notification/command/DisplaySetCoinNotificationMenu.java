package com.github.kegszool.notification.command;

import com.github.kegszool.notification.NotificationContextBuffer;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.annotation.Autowired;

import com.github.kegszool.messaging.util.MessageUtils;
import com.github.kegszool.command.callback.CallbackCommand;

import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.methods.botapimethods.PartialBotApiMethod;

@Component
public class DisplaySetCoinNotificationMenu extends CallbackCommand {

    private final String menuName;
    private final String prefix;
    private final MessageUtils messageUtils;
    private final NotificationContextBuffer contextBuffer;

    @Autowired
    public DisplaySetCoinNotificationMenu(
            @Value("${menu.set_coin_notification.name}") String menuName,
            @Value("${menu.select_coin_notification.prefix}") String prefix,
            MessageUtils messageUtils,
            NotificationContextBuffer contextBuffer
    ) {
        this.menuName = menuName;
        this.prefix = prefix;
        this.messageUtils = messageUtils;
        this.contextBuffer = contextBuffer;
    }

    @Override
    protected boolean canHandleCommand(String command) {
        return command.startsWith(prefix);
    }

    @Override
    protected PartialBotApiMethod<?> handleCommand(CallbackQuery callbackQuery) {
        String coin = callbackQuery.getData().substring(prefix.length());
        contextBuffer.setCoin(callbackQuery.getMessage().getChatId(), coin);
        EditMessageText editMsg = messageUtils.recordAndCreateEditMessageByMenuNameWitCurrentLocal(callbackQuery, menuName);

        String prettyText = editMsg.getText().replace("{coin}", coin);
        editMsg.setText(prettyText);
        return editMsg;
    }
}
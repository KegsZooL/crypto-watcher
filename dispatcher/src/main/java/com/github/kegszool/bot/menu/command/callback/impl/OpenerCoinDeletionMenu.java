package com.github.kegszool.bot.menu.command.callback.impl;

import com.github.kegszool.bot.menu.command.callback.CallbackCommand;
import com.github.kegszool.utils.MessageUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.botapimethods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

@Component
public class OpenerCoinDeletionMenu extends CallbackCommand {

    @Value("${menu.action.open_coin_deletion_menu}")
    private String ACTION_OPEN_COIN_DELETION_MENU;

    @Value("${menu.coin_deletion_menu.name}")
    private String COIN_DELETION_MENU_NAME;

    private final MessageUtils messageUtils;

    @Autowired
    public OpenerCoinDeletionMenu(MessageUtils messageUtils) {
        this.messageUtils = messageUtils;
    }

    @Override
    protected boolean canHandleCommand(String command) {
        return ACTION_OPEN_COIN_DELETION_MENU.equals(command);
    }

    @Override
    protected PartialBotApiMethod<?> handleCommand(CallbackQuery callbackQuery) {
        String chatId = messageUtils.extractChatId(callbackQuery);
        Integer messageId = callbackQuery.getMessage().getMessageId();
        return messageUtils.recordAndCreateEditMessageByMenuName(chatId, messageId, COIN_DELETION_MENU_NAME);
    }
}
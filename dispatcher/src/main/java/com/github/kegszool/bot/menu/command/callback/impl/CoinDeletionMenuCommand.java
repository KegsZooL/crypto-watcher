package com.github.kegszool.bot.menu.command.callback.impl;

import com.github.kegszool.bot.menu.command.callback.CallbackCommand;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.methods.botapimethods.PartialBotApiMethod;

import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Value;

@Component
public class CoinDeletionMenuCommand extends CallbackCommand {

    @Value("${menu.action.open_coin_deletion_menu}")
    private String ACTION_OPEN_COIN_DELETION_MENU;

    @Value("${menu.coin_deletion_menu.name}")
    private String COIN_DELETION_MENU_NAME;

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
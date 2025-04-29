package com.github.kegszool.coin.deletion.command.base;

import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Value;

import com.github.kegszool.command.callback.CallbackCommand;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.methods.botapimethods.PartialBotApiMethod;

@Component
public class DisplayCoinDeletionMenu extends CallbackCommand {

    private final String command;
    private final String menuName;

    public DisplayCoinDeletionMenu(
            @Value("${menu.action.delete_coins}") String command,
            @Value("${menu.coin_deletion_menu.name}") String menuName
    ) {
        this.command = command;
        this.menuName = menuName;
    }

    @Override
    protected boolean canHandleCommand(String command) {
        return this.command.equals(command);
    }

    @Override
    protected PartialBotApiMethod<?> handleCommand(CallbackQuery callbackQuery) {
        String chatId = messageUtils.extractChatId(callbackQuery);
        Integer messageId = callbackQuery.getMessage().getMessageId();
        return messageUtils.recordAndCreateEditMessageByMenuName(chatId, messageId, menuName);
    }
}
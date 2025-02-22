package com.github.kegszool.bot.menu.command.callback.impl.transfer;

import com.github.kegszool.bot.menu.service.transfer.BaseMenuSectionTransfer;
import com.github.kegszool.bot.menu.service.transfer.MenuSectionTransferService;
import com.github.kegszool.bot.menu.service.transfer.impl.CoinDeletionSectionTransfer;

import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.methods.botapimethods.PartialBotApiMethod;

import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.annotation.Autowired;

@Component
public class CoinDeletionMenuCommand extends MenuCommandWithTransfer {

    @Value("${menu.action.open_coin_deletion_menu}")
    private String ACTION_OPEN_COIN_DELETION_MENU;

    @Value("${menu.coin_deletion_menu.name}")
    private String COIN_DELETION_MENU_NAME;

    @Autowired
    public CoinDeletionMenuCommand(MenuSectionTransferService transferService) {
        super(transferService);
    }

    @Override
    protected boolean canHandleCommand(String command) {
        return ACTION_OPEN_COIN_DELETION_MENU.equals(command);
    }

    @Override
    protected Class<? extends BaseMenuSectionTransfer> getTransferClass() {
        return CoinDeletionSectionTransfer.class;
    }

    @Override
    protected PartialBotApiMethod<?> handleMenuCommand(CallbackQuery callbackQuery) {
        String chatId = messageUtils.extractChatId(callbackQuery);
        Integer messageId = callbackQuery.getMessage().getMessageId();
        return messageUtils.recordAndCreateEditMessageByMenuName(chatId, messageId, COIN_DELETION_MENU_NAME);
    }
}
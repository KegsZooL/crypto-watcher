package com.github.kegszool.coin.deletion.handler;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;

import com.github.kegszool.messaging.util.MessageUtils;
import com.github.kegszool.coin.deletion.FavoriteCoinDeletionService;

import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.methods.botapimethods.PartialBotApiMethod;

@Service
public class ConfirmCoinDeletionCommandHandler {

    private final MessageUtils messageUtils;
    private final FavoriteCoinDeletionService deletionService;

    @Value("${menu.coin_deletion_menu.name}")
    private String COIN_DELETION_MENU_NAME;

    public ConfirmCoinDeletionCommandHandler(MessageUtils messageUtils, FavoriteCoinDeletionService deletionService) {
        this.messageUtils = messageUtils;
        this.deletionService = deletionService;
    }

    public PartialBotApiMethod<?> delete(CallbackQuery callbackQuery) {
        deletionService.deleteSelectedCoins(callbackQuery);
        return messageUtils.createEditMessageByMenuName(callbackQuery, COIN_DELETION_MENU_NAME);
    }
}
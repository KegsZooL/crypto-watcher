package com.github.kegszool.coin.deletion.handler;

import com.github.kegszool.messaging.util.MessageUtils;
import com.github.kegszool.coin.deletion.CoinDeletionProcessor;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;
import org.telegram.telegrambots.meta.api.methods.botapimethods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

@Service
public class CoinDeletionConfirmationHandler {

    @Value("${menu.coin_deletion_menu.name}")
    private String COIN_DELETION_MENU_NAME;

    private final MessageUtils messageUtils;
    private final CoinDeletionProcessor coinDeletionProcessor;

    public CoinDeletionConfirmationHandler(MessageUtils messageUtils, CoinDeletionProcessor coinDeletionProcessor) {
        this.messageUtils = messageUtils;
        this.coinDeletionProcessor = coinDeletionProcessor;
    }

    //TODO: update sections after deleting
    public PartialBotApiMethod<?> delete(CallbackQuery callbackQuery) {
        coinDeletionProcessor.processCoinDeletion(callbackQuery, COIN_DELETION_MENU_NAME);
        return messageUtils.createEditMessageByMenuName(callbackQuery, COIN_DELETION_MENU_NAME);
    }
}
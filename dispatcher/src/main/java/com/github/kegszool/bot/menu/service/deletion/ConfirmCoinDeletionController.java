package com.github.kegszool.bot.menu.service.deletion;

import com.github.kegszool.utils.MessageUtils;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;
import org.telegram.telegrambots.meta.api.methods.botapimethods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

@Service
public class ConfirmCoinDeletionController {

    @Value("${menu.coin_deletion_menu.name}")
    private String COIN_DELETION_MENU_NAME;

    private final MessageUtils messageUtils;
    private final CoinDeletionService coinDeletionService;

    public ConfirmCoinDeletionController(MessageUtils messageUtils, CoinDeletionService coinDeletionService) {
        this.messageUtils = messageUtils;
        this.coinDeletionService = coinDeletionService;
    }

    //TODO: update sections after deleting
    public PartialBotApiMethod<?> delete(CallbackQuery callbackQuery) {
        coinDeletionService.processCoinDeletion(callbackQuery, COIN_DELETION_MENU_NAME);
        return messageUtils.createEditMessageByMenuName(callbackQuery, COIN_DELETION_MENU_NAME);
    }
}
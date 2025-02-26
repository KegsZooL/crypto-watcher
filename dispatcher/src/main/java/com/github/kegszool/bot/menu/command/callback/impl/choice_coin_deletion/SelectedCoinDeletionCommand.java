package com.github.kegszool.bot.menu.command.callback.impl.choice_coin_deletion;

import com.github.kegszool.bot.menu.command.callback.CallbackCommand;
import com.github.kegszool.bot.menu.service.selection.controller.CoinDeletionController;

import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.methods.botapimethods.PartialBotApiMethod;

import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.annotation.Autowired;

@Component
public class SelectedCoinDeletionCommand extends CallbackCommand {

    @Value("${menu.coin_deletion_menu.prefix.selected_coin_prefix}")
    private String SELECTED_DELETION_COIN_PREFIX;

    @Value("${menu.coin_deletion_menu.name}")
    private String COIN_DELETION_MENU_NAME;

    private final CoinDeletionController coinDeletionController;

    @Autowired
    public SelectedCoinDeletionCommand(CoinDeletionController coinDeletionController) {
        this.coinDeletionController = coinDeletionController;
    }

    @Override
    protected PartialBotApiMethod<?> handleCommand(CallbackQuery callback) {
        return coinDeletionController.handleSelection(callback, COIN_DELETION_MENU_NAME);
    }

    @Override
    protected boolean canHandleCommand(String command) {
        return command.startsWith(SELECTED_DELETION_COIN_PREFIX);
    }
}
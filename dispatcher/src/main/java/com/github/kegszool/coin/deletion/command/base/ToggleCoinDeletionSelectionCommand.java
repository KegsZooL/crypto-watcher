package com.github.kegszool.coin.deletion.command.base;

import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.annotation.Autowired;

import com.github.kegszool.command.callback.CallbackCommand;
import com.github.kegszool.coin.deletion.service.CoinDeletionSelectionHandler;

import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.methods.botapimethods.PartialBotApiMethod;

@Component
public class ToggleCoinDeletionSelectionCommand extends CallbackCommand {

    private final CoinDeletionSelectionHandler interactionHandler;

    @Value("${menu.coin_deletion_menu.prefix.selected_coin_prefix}")
    private String SELECTED_DELETION_COIN_PREFIX;

    @Value("${menu.coin_deletion_menu.prefix.unselected_coin_prefix}")
    private String UNSELECTED_DELETION_COIN_PREFIX;

    @Value("${menu.coin_deletion_menu.name}")
    private String COIN_DELETION_MENU_NAME;

    @Autowired
    public ToggleCoinDeletionSelectionCommand(CoinDeletionSelectionHandler interactionHandler) {
        this.interactionHandler = interactionHandler;
    }

    @Override
    protected PartialBotApiMethod<?> handleCommand(CallbackQuery callback) {
        return interactionHandler.handleSelection(callback, COIN_DELETION_MENU_NAME);
    }

    @Override
    protected boolean canHandleCommand(String command) {
        return command.startsWith(SELECTED_DELETION_COIN_PREFIX) || command.startsWith(UNSELECTED_DELETION_COIN_PREFIX);
    }
}
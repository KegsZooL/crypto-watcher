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

    private final String menuName;
    private final String selectedPrefix;
    private final String unSelectedPrefix;
    private final CoinDeletionSelectionHandler interactionHandler;

    @Autowired
    public ToggleCoinDeletionSelectionCommand(
            CoinDeletionSelectionHandler interactionHandler,
    		@Value("${menu.coin_deletion_menu.name}") String menuName,
            @Value("${menu.coin_deletion_menu.prefix.selected_coin_prefix}") String selectedPrefix,
            @Value("${menu.coin_deletion_menu.prefix.unselected_coin_prefix}") String unSelectedPrefix
    ) {
        this.menuName = menuName;
        this.selectedPrefix = selectedPrefix;
        this.unSelectedPrefix = unSelectedPrefix;
        this.interactionHandler = interactionHandler;
    }

    @Override
    protected PartialBotApiMethod<?> handleCommand(CallbackQuery callback) {
        return interactionHandler.handleSelection(callback, menuName);
    }

    @Override
    protected boolean canHandleCommand(String command) {
        return command.startsWith(selectedPrefix) || command.startsWith(unSelectedPrefix);
    }
}
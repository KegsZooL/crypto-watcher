package com.github.kegszool.coin.deletion.command;

import com.github.kegszool.menu.service.MenuSelectionHandler;
import com.github.kegszool.command.callback.CallbackCommand;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.methods.botapimethods.PartialBotApiMethod;

import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.annotation.Autowired;

@Component
public class MarkCoinForDeletionCommand extends CallbackCommand {

    @Value("${menu.coin_deletion_menu.prefix.selected_coin_prefix}")
    private String SELECTED_DELETION_COIN_PREFIX;

    @Value("${menu.coin_deletion_menu.name}")
    private String COIN_DELETION_MENU_NAME;

    private final MenuSelectionHandler menuSelectionHandler;

    @Autowired
    public MarkCoinForDeletionCommand(MenuSelectionHandler menuSelectionHandler) {
        this.menuSelectionHandler = menuSelectionHandler;
    }

    @Override
    protected PartialBotApiMethod<?> handleCommand(CallbackQuery callback) {
        return menuSelectionHandler.handleSelection(callback, COIN_DELETION_MENU_NAME);
    }

    @Override
    protected boolean canHandleCommand(String command) {
        return command.startsWith(SELECTED_DELETION_COIN_PREFIX);
    }
}
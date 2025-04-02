package com.github.kegszool.bot.menu.command.callback.impl;

import com.github.kegszool.bot.menu.command.callback.CallbackCommand;
import com.github.kegszool.bot.menu.service.deletion.ConfirmCoinDeletionController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Value;
import org.telegram.telegrambots.meta.api.methods.botapimethods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

@Component
public class ConfirmCoinDeletionCommand extends CallbackCommand {

    @Value("${menu.action.delete_selected}")
    private String DELETE_SELECTED_COIN_CALLBACK_DATA;

    private final ConfirmCoinDeletionController confirmCoinDeletionController;

    @Autowired
    public ConfirmCoinDeletionCommand(ConfirmCoinDeletionController confirmCoinDeletionController) {
        this.confirmCoinDeletionController = confirmCoinDeletionController;
    }

    @Override
    protected boolean canHandleCommand(String command) {
        return DELETE_SELECTED_COIN_CALLBACK_DATA.equals(command);
    }

    @Override
    protected PartialBotApiMethod<?> handleCommand(CallbackQuery callbackQuery) {
        return confirmCoinDeletionController.delete(callbackQuery);
    }
}
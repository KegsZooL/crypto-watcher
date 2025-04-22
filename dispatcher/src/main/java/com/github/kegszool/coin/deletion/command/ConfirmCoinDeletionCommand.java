package com.github.kegszool.coin.deletion.command;

import com.github.kegszool.command.callback.CallbackCommand;
import com.github.kegszool.coin.deletion.handler.CoinDeletionConfirmCommandHandler;

import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.annotation.Autowired;

import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.methods.botapimethods.PartialBotApiMethod;

@Component
public class ConfirmCoinDeletionCommand extends CallbackCommand {

    private final CoinDeletionConfirmCommandHandler coinDeletionConfirmCommandHandler;

    @Value("${menu.action.delete_selected}")
    private String DELETE_SELECTED_COIN_CALLBACK_DATA;

    @Autowired
    public ConfirmCoinDeletionCommand(CoinDeletionConfirmCommandHandler coinDeletionConfirmCommandHandler) {
        this.coinDeletionConfirmCommandHandler = coinDeletionConfirmCommandHandler;
    }

    @Override
    protected boolean canHandleCommand(String command) {
        return DELETE_SELECTED_COIN_CALLBACK_DATA.equals(command);
    }

    @Override
    protected PartialBotApiMethod<?> handleCommand(CallbackQuery callbackQuery) {
        return coinDeletionConfirmCommandHandler.delete(callbackQuery);
    }
}
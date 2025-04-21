package com.github.kegszool.coin.deletion.command;

import com.github.kegszool.coin.deletion.handler.CoinDeletionConfirmationHandler;
import com.github.kegszool.command.callback.CallbackCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Value;
import org.telegram.telegrambots.meta.api.methods.botapimethods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

@Component
public class ConfirmCoinDeletionCommand extends CallbackCommand {

    @Value("${menu.action.delete_selected}")
    private String DELETE_SELECTED_COIN_CALLBACK_DATA;

    private final CoinDeletionConfirmationHandler coinDeletionConfirmationHandler;

    @Autowired
    public ConfirmCoinDeletionCommand(CoinDeletionConfirmationHandler coinDeletionConfirmationHandler) {
        this.coinDeletionConfirmationHandler = coinDeletionConfirmationHandler;
    }

    @Override
    protected boolean canHandleCommand(String command) {
        return DELETE_SELECTED_COIN_CALLBACK_DATA.equals(command);
    }

    @Override
    protected PartialBotApiMethod<?> handleCommand(CallbackQuery callbackQuery) {
        return coinDeletionConfirmationHandler.delete(callbackQuery);
    }
}
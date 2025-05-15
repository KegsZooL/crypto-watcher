package com.github.kegszool.coin.deletion.command.base;

import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.annotation.Autowired;

import com.github.kegszool.command.callback.CallbackCommand;
import com.github.kegszool.coin.deletion.command.ConfirmCoinDeletionCommandHandler;

import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.methods.botapimethods.PartialBotApiMethod;

@Component
public class ConfirmCoinDeletionCommand extends CallbackCommand {

    private final String command;
    private final ConfirmCoinDeletionCommandHandler confirmCoinDeletionCommandHandler;

    @Autowired
    public ConfirmCoinDeletionCommand(
            @Value("${menu.action.delete_selected}") String command,
            ConfirmCoinDeletionCommandHandler confirmCoinDeletionCommandHandler
    ) {
        this.confirmCoinDeletionCommandHandler = confirmCoinDeletionCommandHandler;
        this.command = command;
    }

    @Override
    protected boolean canHandleCommand(String command) {
        return this.command.equals(command);
    }

    @Override
    protected PartialBotApiMethod<?> handleCommand(CallbackQuery callbackQuery) {
        return confirmCoinDeletionCommandHandler.delete(callbackQuery);
    }
}
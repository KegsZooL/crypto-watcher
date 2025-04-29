package com.github.kegszool.coin.addition.command.base;

import com.github.kegszool.coin.addition.command.CoinAdditionCommandHandler;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Value;

import com.github.kegszool.command.text.TextCommand;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.methods.botapimethods.PartialBotApiMethod;

@Component
public class AddCoinCommand extends TextCommand {

    private final String command;
    private final CoinAdditionCommandHandler handler;

    public AddCoinCommand(@Value("${bot.command.add_coin}") String command,
                          CoinAdditionCommandHandler handler) {
        this.command = command;
        this.handler = handler;
    }

    @Override
    protected boolean canHandleCommand(String command) {
        return command.startsWith(this.command);
    }

    @Override
    protected PartialBotApiMethod<?> handleCommand(Update update) {
        return handler.handle(update);
    }
}
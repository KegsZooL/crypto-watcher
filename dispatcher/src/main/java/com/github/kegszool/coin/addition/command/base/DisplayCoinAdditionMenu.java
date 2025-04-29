package com.github.kegszool.coin.addition.command.base;

import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.annotation.Autowired;

import com.github.kegszool.messaging.util.MessageUtils;
import com.github.kegszool.command.callback.CallbackCommand;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.methods.botapimethods.PartialBotApiMethod;

@Component
public class DisplayCoinAdditionMenu extends CallbackCommand {

    private final String command;
    private final String menuName;
    private final MessageUtils messageUtils;

    @Autowired
    public DisplayCoinAdditionMenu(
           @Value("${menu.action.add_coin}") String command,
           @Value("${menu.coin_addition.name}") String menuName,
           MessageUtils messageUtils
    ) {
        this.command = command;
        this.menuName = menuName;
        this.messageUtils = messageUtils;
    }

    @Override
    protected boolean canHandleCommand(String command) {
        return this.command.equals(command);
    }

    @Override
    protected PartialBotApiMethod<?> handleCommand(CallbackQuery callbackQuery) {
        return messageUtils.createEditMessageByMenuName(callbackQuery, menuName);
    }
}
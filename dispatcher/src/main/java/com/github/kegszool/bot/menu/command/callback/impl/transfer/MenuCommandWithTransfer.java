package com.github.kegszool.bot.menu.command.callback.impl.transfer;

import com.github.kegszool.bot.menu.command.callback.CallbackCommand;
import com.github.kegszool.bot.menu.service.transfer.BaseMenuSectionTransfer;
import com.github.kegszool.bot.menu.service.transfer.MenuSectionTransferService;

import org.telegram.telegrambots.meta.api.methods.botapimethods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;

@Component
public abstract class MenuCommandWithTransfer extends CallbackCommand {

    private final MenuSectionTransferService menuSectionTransferService;

    @Autowired
    public MenuCommandWithTransfer(MenuSectionTransferService menuSectionTransferService) {
        this.menuSectionTransferService = menuSectionTransferService;
    }

    protected abstract Class<? extends BaseMenuSectionTransfer> getTransferClass();

    @Override
    protected PartialBotApiMethod<?> handleCommand(CallbackQuery callbackQuery) {
        menuSectionTransferService.performTransfer(getTransferClass());
        return handleMenuCommand(callbackQuery);
    }

    protected abstract PartialBotApiMethod<?> handleMenuCommand(CallbackQuery callbackQuery);
}
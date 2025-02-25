package com.github.kegszool.bot.menu.command.callback.impl.with_section_transfer;

import com.github.kegszool.bot.menu.command.callback.CallbackCommand;
import com.github.kegszool.bot.menu.service.section_transfer.BaseMenuSectionTransfer;
import com.github.kegszool.bot.menu.service.section_transfer.MenuSectionTransferService;

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
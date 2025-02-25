package com.github.kegszool.bot.menu.command.callback.impl;

import com.github.kegszool.bot.menu.command.callback.CallbackCommand;
import com.github.kegszool.bot.menu.command.callback.impl.entity.PriceSnapshotProperties;

import com.github.kegszool.bot.menu.service.PriceSnapshotParameterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import org.telegram.telegrambots.meta.api.methods.botapimethods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

@Component
public class PriceSnapshotParameterMenuCommand extends CallbackCommand {

    private final PriceSnapshotProperties snapshotProperties;
    private final PriceSnapshotParameterService snapshotParameterService;

    @Autowired
    public PriceSnapshotParameterMenuCommand(
            PriceSnapshotProperties snapshotProperties,
            PriceSnapshotParameterService snapshotParameterService
    ) {
        this.snapshotProperties = snapshotProperties;
        this.snapshotParameterService = snapshotParameterService;
    }

    @Override
    protected boolean canHandleCommand(String command) {
        return command.startsWith(snapshotProperties.getParameterPrefix());
    }

    @Override
    protected PartialBotApiMethod<?> handleCommand(CallbackQuery query) {
        return snapshotParameterService.createPriceSnapshotParameterMessage(query, snapshotProperties);
    }
}
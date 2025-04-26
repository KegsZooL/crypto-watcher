package com.github.kegszool.coin.price.command;

import com.github.kegszool.coin.price.util.PriceParameterMessageBuilder;
import com.github.kegszool.coin.price.menu.PriceMenuProperties;
import com.github.kegszool.command.callback.CallbackCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.methods.botapimethods.PartialBotApiMethod;

@Component
public class DisplayPriceParameterCommand extends CallbackCommand {

    private final PriceMenuProperties properties;
    private final PriceParameterMessageBuilder parameterService;

    @Autowired
    public DisplayPriceParameterCommand(
            PriceMenuProperties properties,
            PriceParameterMessageBuilder parameterService
    ) {
        this.properties = properties;
        this.parameterService = parameterService;
    }

    @Override
    protected boolean canHandleCommand(String command) {
        return command.startsWith(properties.getParameterPrefix());
    }

    @Override
    protected PartialBotApiMethod<?> handleCommand(CallbackQuery query) {
        return parameterService.createPriceParameterMessage(query, properties);
    }
}
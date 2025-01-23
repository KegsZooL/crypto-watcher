package com.github.kegszool.bot.menu.command.callback.impl;

import com.github.kegszool.bot.handler.response.impl.PriceSnapshotResponseHandler;
import com.github.kegszool.bot.menu.command.callback.CallbackCommand;
import com.github.kegszool.messaging.dto.CoinPriceSnapshot;
import com.github.kegszool.utils.MessageUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.botapimethods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

@Component
public class PriceSnapshotParameterCommand extends CallbackCommand {

    @Value("${menu.price_snapshot.name}")
    private String PRICE_SNAPSHOT_MENU_NAME;

    @Value("${menu.price_snapshot.parameters.prefix}")
    private String PARAMETER_PREFIX;

    private final MessageUtils messageUtils;
    private final PriceSnapshotResponseHandler coinPriceSnapshotResponseHandler;

    @Autowired
    public PriceSnapshotParameterCommand(MessageUtils messageUtils, PriceSnapshotResponseHandler coinPriceSnapshotResponseHandler) {
        this.messageUtils = messageUtils;
        this.coinPriceSnapshotResponseHandler = coinPriceSnapshotResponseHandler;
    }

    @Override
    protected boolean canHandleCommand(String command) {
        return command.startsWith(PARAMETER_PREFIX);
    }

    @Override
    protected PartialBotApiMethod<?> handleCommand(CallbackQuery query) {
        String parameter = getParameterWithoutPrefix(query);
        String chatId = messageUtils.extractChatId(query);
        CoinPriceSnapshot priceSnapshot = coinPriceSnapshotResponseHandler.getCoinPriceSnapshot(chatId);

        String parameterValue = getParameterValue(parameter, priceSnapshot);
        var answerMessage = messageUtils.createEditMessageByMenuName(query, parameterValue, PRICE_SNAPSHOT_MENU_NAME);
        return answerMessage;
    }

    private String getParameterWithoutPrefix(CallbackQuery query) {
        String data = query.getData();
        return data.substring(PARAMETER_PREFIX.length());
    }

    private String getParameterValue(String parameter, CoinPriceSnapshot priceSnapshot) {
        switch (parameter) {
            case "max_price_24h":
                return "Max price for 24h: " + priceSnapshot.getMaxPrice24h();
            case "min_price_24h":
                return "Min price for 24h: " + priceSnapshot.getMinPrice24h();
            case "trading_volume":
                return "Trading volume in 24h [currency unit]: " + priceSnapshot.getTradingVolume24h();
            case "trading_volume_currency":
                return "Trading volume in 24h [contract unit]: " + priceSnapshot.getTradingVolumeCurrency24h();
            default:
                return "Unknown parameter";
        }
    }
}
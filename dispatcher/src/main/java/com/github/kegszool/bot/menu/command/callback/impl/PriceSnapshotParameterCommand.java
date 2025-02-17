package com.github.kegszool.bot.menu.command.callback.impl;

import com.github.kegszool.bot.menu.command.callback.CallbackCommand;
import com.github.kegszool.bot.handler.response.exchange.PriceSnapshotResponseHandler;
import com.github.kegszool.bot.menu.command.callback.impl.entity.PriceSnapshotProperties;

import com.github.kegszool.messaging.dto.command_entity.PriceSnapshot;
import com.github.kegszool.utils.MessageUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.botapimethods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

import java.util.Map;
import java.util.function.Function;

@Component
public class PriceSnapshotParameterCommand extends CallbackCommand {

    private final MessageUtils messageUtils;
    private final PriceSnapshotResponseHandler priceSnapshotResponseHandler;
    private final PriceSnapshotProperties properties;

    @Autowired
    public PriceSnapshotParameterCommand(
            MessageUtils messageUtils,
            @Lazy PriceSnapshotResponseHandler priceSnapshotResponseHandler,
            PriceSnapshotProperties properties
    ) {
        this.messageUtils = messageUtils;
        this.priceSnapshotResponseHandler = priceSnapshotResponseHandler;
        this.properties = properties;
    }

    @Override
    protected boolean canHandleCommand(String command) {
        return command.startsWith(properties.getParameterPrefix());
    }

    @Override
    protected PartialBotApiMethod<?> handleCommand(CallbackQuery query) {
        String parameter = getParameterWithoutPrefix(query);
        String chatId = messageUtils.extractChatId(query);

        var priceSnapshot = priceSnapshotResponseHandler.getCoinPriceSnapshot(chatId);
        var parameterMap = createParameterMap();

        String title = createTittleByParameter(parameter, parameterMap, priceSnapshot);

        var answerMessage = messageUtils.createEditMessageByMenuName(query, title, properties.getMenuName());
        answerMessage.setParseMode(ParseMode.HTML);
        return answerMessage;
    }

    private String getParameterWithoutPrefix(CallbackQuery query) {
        String data = query.getData();
        return data.substring(properties.getParameterPrefix().length());
    }

    private Map<String, ParameterInfo> createParameterMap() {
        return Map.of(
            properties.getLastPriceName(), new ParameterInfo(
                    properties.getLastPriceDescription(), snapshot -> "$" + snapshot.getLastPrice()),
            properties.getHighestPrice24hName(), new ParameterInfo(
                    properties.getHighestPrice24hDescription(), snapshot -> "$" + snapshot.getMaxPrice24h()),
            properties.getLowestPrice24hName(), new ParameterInfo(
                    properties.getLowestPrice24hDescription(), snapshot -> "$" + snapshot.getMinPrice24h()),
            properties.getTradingVolumeName(), new ParameterInfo(
                    properties.getTradingVolumeDescription(), snapshot -> String.valueOf(snapshot.getTradingVolume24h())),
            properties.getTradingVolumeCurrencyName(), new ParameterInfo(
                    properties.getTradingVolumeCurrencyDescription(), snapshot -> String.valueOf(snapshot.getTradingVolumeCurrency24h())
                )
        );
    }

    private String createTittleByParameter(
            String desiredParameter,
            Map<String, ParameterInfo> createParameterMap,
            PriceSnapshot priceSnapshot
    ) {
        ParameterInfo parameterInfo = createParameterMap.get(desiredParameter);

        String coin = priceSnapshot.getName();
        String value = parameterInfo.getValue(priceSnapshot);
        String description = parameterInfo.getDescription();

        String format = "\uD83E\uDE99 — <b>%s</b>\n\n%s — <b>%s</b>";
        return String.format(format, coin, description, value);
    }

    private static class ParameterInfo {

        private final String description;
        private final Function<PriceSnapshot, String> valueProvider;

        public ParameterInfo(String description, Function<PriceSnapshot, String> valueProvider) {
            this.description = description;
            this.valueProvider = valueProvider;
        }

        public String getDescription() {
            return description;
        }

        public String getValue(PriceSnapshot coinPriceSnapshot) {
            return valueProvider.apply(coinPriceSnapshot);
        }
    }
}
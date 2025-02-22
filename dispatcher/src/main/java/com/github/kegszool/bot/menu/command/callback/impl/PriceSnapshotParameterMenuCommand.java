package com.github.kegszool.bot.menu.command.callback.impl;

import com.github.kegszool.bot.menu.command.callback.CallbackCommand;
import com.github.kegszool.bot.handler.response.exchange.PriceSnapshotResponseHandler;
import com.github.kegszool.bot.menu.command.callback.impl.entity.PriceSnapshotParameterInfo;
import com.github.kegszool.bot.menu.command.callback.impl.entity.PriceSnapshotProperties;

import com.github.kegszool.messaging.dto.command_entity.PriceSnapshot;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.botapimethods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

import java.util.Map;

//TODO division responsibilities

@Component
public class PriceSnapshotParameterMenuCommand extends CallbackCommand {

    @Value("${emoji_unicode_symbol.coin}")
    private String COIN_EMOJI_UNICODE_SYMBOL;

    private final PriceSnapshotResponseHandler priceSnapshotResponseHandler;
    private final PriceSnapshotProperties properties;

    @Autowired
    public PriceSnapshotParameterMenuCommand(
            @Lazy PriceSnapshotResponseHandler priceSnapshotResponseHandler,
            PriceSnapshotProperties properties
    ) {
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

    private Map<String, PriceSnapshotParameterInfo> createParameterMap() {
        return Map.of(
            properties.getLastPriceName(), new PriceSnapshotParameterInfo(
                    properties.getLastPriceDescription(), snapshot -> "$" + snapshot.getLastPrice()),
            properties.getHighestPrice24hName(), new PriceSnapshotParameterInfo(
                    properties.getHighestPrice24hDescription(), snapshot -> "$" + snapshot.getMaxPrice24h()),
            properties.getLowestPrice24hName(), new PriceSnapshotParameterInfo(
                    properties.getLowestPrice24hDescription(), snapshot -> "$" + snapshot.getMinPrice24h()),
            properties.getTradingVolumeName(), new PriceSnapshotParameterInfo(
                    properties.getTradingVolumeDescription(), snapshot -> String.valueOf(snapshot.getTradingVolume24h())),
            properties.getTradingVolumeCurrencyName(), new PriceSnapshotParameterInfo(
                    properties.getTradingVolumeCurrencyDescription(), snapshot -> String.valueOf(snapshot.getTradingVolumeCurrency24h())
                )
        );
    }

    private String createTittleByParameter(
            String desiredParameter,
            Map<String, PriceSnapshotParameterInfo> createParameterMap,
            PriceSnapshot priceSnapshot
    ) {
        PriceSnapshotParameterInfo parameterInfo = createParameterMap.get(desiredParameter);

        String coin = priceSnapshot.getName();
        String value = parameterInfo.getValue(priceSnapshot);
        String description = parameterInfo.getDescription();

        String format = COIN_EMOJI_UNICODE_SYMBOL + " — <b>%s</b>\n\n%s — <b>%s</b>";
        return String.format(format, coin, description, value);
    }
}
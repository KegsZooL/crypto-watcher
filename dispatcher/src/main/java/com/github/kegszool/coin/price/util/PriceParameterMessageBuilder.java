package com.github.kegszool.coin.price.util;

import java.util.Map;
import org.springframework.stereotype.Component;
import org.springframework.context.annotation.Lazy;
import org.springframework.beans.factory.annotation.Autowired;

import com.github.kegszool.messaging.util.MessageUtils;
import com.github.kegszool.coin.price.model.PriceParameter;
import com.github.kegszool.coin.price.menu.PriceMenuProperties;

import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;

import com.github.kegszool.coin.price.model.PriceSnapshot;
import com.github.kegszool.coin.price.model.PriceSnapshotCache;

@Component
public class PriceParameterMessageBuilder {

    private final PriceSnapshotCache priceBuffer;
    private final PriceParameterBuilder snapshotParametersRegistry;
    private final PriceParameterFormatter snapshotParameterFormater;
    private final MessageUtils messageUtils;

    @Autowired
    public PriceParameterMessageBuilder(
            @Lazy PriceSnapshotCache priceBuffer,
            PriceParameterBuilder snapshotParametersRegistry,
            PriceParameterFormatter snapshotParameterFormater,
            MessageUtils messageUtils
    ) {
        this.priceBuffer = priceBuffer;
        this.snapshotParametersRegistry = snapshotParametersRegistry;
        this.snapshotParameterFormater = snapshotParameterFormater;
        this.messageUtils = messageUtils;
    }

    public EditMessageText createPriceParameterMessage(
            CallbackQuery callbackQuery,
            PriceMenuProperties snapshotProperties
    ) {
        String chatId = messageUtils.extractChatId(callbackQuery);
        String parameterWithoutPrefix = getParameterWithoutPrefix(callbackQuery, snapshotProperties);

        PriceSnapshot snapshot = priceBuffer.get(chatId);
        Map<String, PriceParameter> snapshotParameterMap = snapshotParametersRegistry.createParameterMap(snapshotProperties, chatId);

        String title = createTittleByParameter(parameterWithoutPrefix, snapshotParameterMap, snapshot);
        EditMessageText message = messageUtils.createEditMessageByMenuName(callbackQuery, title, snapshotProperties.getMenuName());
        message.setParseMode(ParseMode.HTML);
        return message;
    }

    private String getParameterWithoutPrefix(CallbackQuery query, PriceMenuProperties snapshotProperties) {
        String data = query.getData();
        return data.substring(snapshotProperties.getParameterPrefix().length());
    }

    private String createTittleByParameter(
            String parameter,
            Map<String, PriceParameter> snapshotParameterMap,
            PriceSnapshot priceSnapshot
    ) {

        PriceParameter parameterInfo = snapshotParameterMap.get(parameter);
        String coinName = priceSnapshot.getName();
        String parameterValue = parameterInfo.getValue(priceSnapshot);
        String description = parameterInfo.getDescription();

        return snapshotParameterFormater.formatTitle(coinName, parameterValue, description);
    }
}
package com.github.kegszool.coin.price.util;

import org.springframework.stereotype.Component;
import org.springframework.context.annotation.Lazy;
import org.springframework.beans.factory.annotation.Autowired;

import com.github.kegszool.coin.price.model.CoinPrice;
import com.github.kegszool.messaging.util.MessageUtils;
import com.github.kegszool.coin.price.model.PriceBuffer;
import com.github.kegszool.coin.price.model.PriceParameter;
import com.github.kegszool.coin.price.menu.PriceMenuProperties;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;

import java.util.Map;

@Component
public class PriceParameterMessageBuilder {

    private final PriceBuffer snapshotRepository;
    private final PriceParameterBuilder snapshotParametersRegistry;
    private final PriceParameterFormatter snapshotParameterFormater;
    private final MessageUtils messageUtils;

    @Autowired
    public PriceParameterMessageBuilder(
            @Lazy PriceBuffer snapshotRepository,
            PriceParameterBuilder snapshotParametersRegistry,
            PriceParameterFormatter snapshotParameterFormater,
            MessageUtils messageUtils
    ) {
        this.snapshotRepository = snapshotRepository;
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

        CoinPrice snapshot = snapshotRepository.getSnapshot(chatId);
        Map<String, PriceParameter> snapshotParameterMap = snapshotParametersRegistry.createParameterMap(snapshotProperties);

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
            CoinPrice coinPrice
    ) {

        PriceParameter parameterInfo = snapshotParameterMap.get(parameter);
        String coinName = coinPrice.getName();
        String parameterValue = parameterInfo.getValue(coinPrice);
        String description = parameterInfo.getDescription();

        return snapshotParameterFormater.formatTitle(coinName, parameterValue, description);
    }
}
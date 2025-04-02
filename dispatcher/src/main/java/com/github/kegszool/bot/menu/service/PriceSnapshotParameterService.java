package com.github.kegszool.bot.menu.service;

import com.github.kegszool.utils.MessageUtils;
import com.github.kegszool.messaging.dto.command_entity.CoinPriceSnapshot;

import com.github.kegszool.bot.menu.entity.PriceSnapshotProperties;
import com.github.kegszool.bot.menu.entity.PriceSnapshotParameterInfo;
import com.github.kegszool.bot.menu.service.price_snapshot.PriceSnapshotRepository;

import com.github.kegszool.bot.menu.service.price_snapshot.PriceSnapshotParameterFormater;
import com.github.kegszool.bot.menu.service.price_snapshot.PriceSnapshotParametersRegistry;

import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;

import org.springframework.stereotype.Service;
import org.springframework.context.annotation.Lazy;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

@Service
public class PriceSnapshotParameterService {

    private final PriceSnapshotRepository snapshotRepository;
    private final PriceSnapshotParametersRegistry snapshotParametersRegistry;
    private final PriceSnapshotParameterFormater snapshotParameterFormater;
    private final MessageUtils messageUtils;

    @Autowired
    public PriceSnapshotParameterService(
            @Lazy PriceSnapshotRepository snapshotRepository,
            PriceSnapshotParametersRegistry snapshotParametersRegistry,
            PriceSnapshotParameterFormater snapshotParameterFormater,
            MessageUtils messageUtils
    ) {
        this.snapshotRepository = snapshotRepository;
        this.snapshotParametersRegistry = snapshotParametersRegistry;
        this.snapshotParameterFormater = snapshotParameterFormater;
        this.messageUtils = messageUtils;
    }

    public EditMessageText createPriceSnapshotParameterMessage(
            CallbackQuery callbackQuery,
            PriceSnapshotProperties snapshotProperties
    ) {
        String chatId = messageUtils.extractChatId(callbackQuery);
        String parameterWithoutPrefix = getParameterWithoutPrefix(callbackQuery, snapshotProperties);

        CoinPriceSnapshot snapshot = snapshotRepository.getSnapshot(chatId);
        Map<String, PriceSnapshotParameterInfo> snapshotParameterMap = snapshotParametersRegistry.createParameterMap(snapshotProperties);

        String title = createTittleByParameter(parameterWithoutPrefix, snapshotParameterMap, snapshot);
        EditMessageText message = messageUtils.createEditMessageByMenuName(callbackQuery, title, snapshotProperties.getMenuName());
        message.setParseMode(ParseMode.HTML);
        return message;
    }

    private String getParameterWithoutPrefix(CallbackQuery query, PriceSnapshotProperties snapshotProperties) {
        String data = query.getData();
        return data.substring(snapshotProperties.getParameterPrefix().length());
    }

    private String createTittleByParameter(
            String parameter,
            Map<String, PriceSnapshotParameterInfo> snapshotParameterMap,
            CoinPriceSnapshot coinPriceSnapshot
    ) {

        PriceSnapshotParameterInfo parameterInfo = snapshotParameterMap.get(parameter);
        String coinName = coinPriceSnapshot.getName();
        String parameterValue = parameterInfo.getValue(coinPriceSnapshot);
        String description = parameterInfo.getDescription();

        return snapshotParameterFormater.formatTitle(coinName, parameterValue, description);
    }
}
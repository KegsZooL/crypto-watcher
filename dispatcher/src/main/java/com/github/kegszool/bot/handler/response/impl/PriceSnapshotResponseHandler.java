package com.github.kegszool.bot.handler.response.impl;

import com.github.kegszool.bot.handler.response.BaseResponseHandler;
import com.github.kegszool.messaging.dto.PriceSnapshot;
import com.github.kegszool.messaging.dto.ServiceMessage;
import com.github.kegszool.utils.MessageUtils;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.io.StreamIterator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.botapimethods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class PriceSnapshotResponseHandler extends BaseResponseHandler {

    @Value("${spring.rabbitmq.template.routing-key.coin_price_response}")
    private String COIN_PRICE_RESPONSE_ROUTING_KEY;

    @Value("${menu.price_snapshot.name}")
    private String PRICE_SNAPSHOT_MENU_NAME;

    private final Map<String, PriceSnapshot> coinPriceSnapshotMap = new ConcurrentHashMap<>();

    @Autowired
    public PriceSnapshotResponseHandler(MessageUtils messageUtils) {
        super(messageUtils);
    }

    @Override
    public boolean canHandle(String routingKey) {
        return COIN_PRICE_RESPONSE_ROUTING_KEY.equals(routingKey);
    }

    @Override
    public PartialBotApiMethod<?> handle(ServiceMessage<?> serviceMessage) {
        String chatId = serviceMessage.getChatId();
        Integer messageId = serviceMessage.getMessageId();

        var priceSnapshot = (PriceSnapshot)serviceMessage.getData();
        coinPriceSnapshotMap.put(chatId, priceSnapshot);

        return createAnswerMessage(priceSnapshot, chatId, messageId);
    }

    private EditMessageText createAnswerMessage(PriceSnapshot snapshot, String chatId, Integer messageId) {
        String coin = snapshot.getName();

        var answerMessage = messageUtils.recordAndCreateEditMessageByMenuName(
                chatId, messageId, PRICE_SNAPSHOT_MENU_NAME
        );
        String currentTitle = answerMessage.getText();
        String newTitle = String.format(currentTitle, coin);
        answerMessage.setText(newTitle);
        return answerMessage;
    }


    public PriceSnapshot getCoinPriceSnapshot(String chatId) {
        return coinPriceSnapshotMap.get(chatId);
    }
}
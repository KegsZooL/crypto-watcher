package com.github.kegszool.bot.handler.response.impl;

import com.github.kegszool.bot.handler.response.BaseResponseHandler;
import com.github.kegszool.messaging.dto.CoinPriceSnapshot;
import com.github.kegszool.messaging.dto.ServiceMessage;
import com.github.kegszool.utils.KeyboardFactory;
import com.github.kegszool.utils.MessageUtils;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.botapimethods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

//TODO: finish the impl the current command

@Component
@Log4j2
public class CoinPriceSnapshotResponseHandler extends BaseResponseHandler {

    @Value("${spring.rabbitmq.template.routing-key.coin_price_response}")
    private String COIN_PRICE_RESPONSE_ROUTING_KEY;

    private final Map<String, CoinPriceSnapshot> coinPriceSnapshotMap = new ConcurrentHashMap<>();

    @Autowired
    public CoinPriceSnapshotResponseHandler(MessageUtils messageUtils) {
        super(messageUtils);
    }

    @Override
    public boolean canHandle(String routingKey) {
        return COIN_PRICE_RESPONSE_ROUTING_KEY.equals(routingKey);
    }

    @Override
    public PartialBotApiMethod<?> handle(ServiceMessage serviceMessage) {

        String chatId = serviceMessage.getChatId();
        var priceSnapshot = (CoinPriceSnapshot)serviceMessage.getData();
        coinPriceSnapshotMap.put(chatId, priceSnapshot);

        double lastPrice = priceSnapshot.getLastPrice();


        Map<String, String> snapshotActionButtonMapping = new LinkedHashMap<>();
        snapshotActionButtonMapping.put("price_snapshot_max_price_24h", "Max price for 24h");
        snapshotActionButtonMapping.put("price_snapshot_min_price_24h", "Min price for 24h");
        snapshotActionButtonMapping.put("price_snapshot_trading_volume", "Trading volume in 24h [currency unit]");
        snapshotActionButtonMapping.put("price_snapshot_trading_volume_currency", "Trading volume in 24h [contract unit]");
        snapshotActionButtonMapping.put("back", "Back");

        var keyboard = KeyboardFactory.create(snapshotActionButtonMapping);

        var answerMessage = new SendMessage(chatId, String.valueOf(lastPrice));
        answerMessage.setReplyMarkup(keyboard);

        return answerMessage;
    }

    public CoinPriceSnapshot getCoinPriceSnapshot(String chatId) {
        return coinPriceSnapshotMap.get(chatId);
    }
}
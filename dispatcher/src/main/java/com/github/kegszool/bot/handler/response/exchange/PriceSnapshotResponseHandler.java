package com.github.kegszool.bot.handler.response.exchange;

import com.github.kegszool.bot.handler.response.BaseResponseHandler;
import com.github.kegszool.bot.handler.result.HandlerResult;
import com.github.kegszool.messaging.dto.command_entity.PriceSnapshot;
import com.github.kegszool.messaging.dto.service.ServiceMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.botapimethods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class PriceSnapshotResponseHandler extends BaseResponseHandler<PriceSnapshot> {

    @Value("${spring.rabbitmq.template.routing-key.coin_price_response}")
    private String COIN_PRICE_RESPONSE_ROUTING_KEY;

    @Value("${menu.price_snapshot.name}")
    private String PRICE_SNAPSHOT_MENU_NAME;

    private final Map<String, PriceSnapshot> coinPriceSnapshotMap = new ConcurrentHashMap<>();

    @Override
    public boolean canHandle(String routingKey) {
        return COIN_PRICE_RESPONSE_ROUTING_KEY.equals(routingKey);
    }

    @Override
    public HandlerResult handle(ServiceMessage<PriceSnapshot> serviceMessage) {
        String chatId = serviceMessage.getChatId();
        Integer messageId = serviceMessage.getMessageId();

        var priceSnapshot = serviceMessage.getData();
        coinPriceSnapshotMap.put(chatId, priceSnapshot);

        var answerMessage = createAnswerMessage(priceSnapshot, chatId, messageId);
        return new HandlerResult.Success(answerMessage);
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
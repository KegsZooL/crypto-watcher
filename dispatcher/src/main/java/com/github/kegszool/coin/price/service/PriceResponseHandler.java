package com.github.kegszool.coin.price.service;

import com.github.kegszool.coin.dto.CoinPrice;
import com.github.kegszool.coin.price.model.PriceBuffer;
import com.github.kegszool.messaging.response.BaseResponseHandler;
import com.github.kegszool.messaging.dto.HandlerResult;
import com.github.kegszool.messaging.dto.ServiceMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.springframework.beans.factory.annotation.Value;

@Service
public class PriceResponseHandler extends BaseResponseHandler<CoinPrice> {

    @Value("${spring.rabbitmq.template.routing-key.coin_price_response}")
    private String COIN_PRICE_RESPONSE_ROUTING_KEY;

    @Value("${menu.price_snapshot.name}")
    private String PRICE_SNAPSHOT_MENU_NAME;

    private final PriceBuffer priceBuffer;

    @Autowired
    public PriceResponseHandler(PriceBuffer priceBuffer) {
        this.priceBuffer = priceBuffer;
    }

    @Override
    public boolean canHandle(String routingKey) {
        return COIN_PRICE_RESPONSE_ROUTING_KEY.equals(routingKey);
    }

    @Override
    public HandlerResult handle(ServiceMessage<CoinPrice> serviceMessage) {
        String chatId = serviceMessage.getChatId();
        Integer messageId = serviceMessage.getMessageId();

        CoinPrice snapshot = serviceMessage.getData();
        priceBuffer.saveSnapshot(chatId, snapshot);

        EditMessageText answerMessage = createAnswerMessage(snapshot, chatId, messageId);
        return new HandlerResult.Success(answerMessage);
    }

    private EditMessageText createAnswerMessage(CoinPrice snapshot, String chatId, Integer messageId) {
        String coin = snapshot.getName();
        var answerMessage = messageUtils.recordAndCreateEditMessageByMenuName(
                chatId, messageId, PRICE_SNAPSHOT_MENU_NAME
        );
        String currentTitle = answerMessage.getText();
        String newTitle = String.format(currentTitle, coin);
        answerMessage.setText(newTitle);
        return answerMessage;
    }
}
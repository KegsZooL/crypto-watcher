package com.github.kegszool.coin.price.service;

import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.annotation.Autowired;

import com.github.kegszool.coin.price.model.CoinPrice;
import com.github.kegszool.coin.price.model.PriceBuffer;

import com.github.kegszool.messaging.dto.HandlerResult;
import com.github.kegszool.messaging.dto.service.ServiceMessage;
import com.github.kegszool.messaging.response.BaseResponseHandler;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;

@Component
public class PriceResponseHandler extends BaseResponseHandler<CoinPrice> {

    private final String routingKey;
    private final String menuName;
    private final PriceBuffer priceBuffer;

    @Autowired
    public PriceResponseHandler(
            @Value("${spring.rabbitmq.template.routing-key.coin_price_response}") String routingKey,
         	@Value("${menu.price_snapshot.name}") String menuName,
            PriceBuffer priceBuffer
    ) {
        this.priceBuffer = priceBuffer;
        this.routingKey = routingKey;
        this.menuName = menuName;
    }

    @Override
    public boolean canHandle(String routingKey) {
        return this.routingKey.equals(routingKey);
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
        EditMessageText answerMessage = messageUtils.recordAndCreateEditMessageByMenuName(
                chatId, messageId, menuName
        );
        String currentTitle = answerMessage.getText();
        String newTitle = String.format(currentTitle, coin);
        answerMessage.setText(newTitle);
        return answerMessage;
    }
}
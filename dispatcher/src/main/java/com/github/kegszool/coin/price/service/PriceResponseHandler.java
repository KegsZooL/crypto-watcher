package com.github.kegszool.coin.price.service;

import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.annotation.Autowired;

import com.github.kegszool.coin.price.model.PriceSnapshot;
import com.github.kegszool.coin.price.model.PriceSnapshotCache;

import com.github.kegszool.messaging.dto.HandlerResult;
import com.github.kegszool.messaging.dto.service.ServiceMessage;
import com.github.kegszool.messaging.response.BaseResponseHandler;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;

@Component
public class PriceResponseHandler extends BaseResponseHandler<PriceSnapshot> {

    private final String routingKey;
    private final String menuName;
    private final PriceSnapshotCache priceBuffer;

    @Autowired
    public PriceResponseHandler(
            @Value("${spring.rabbitmq.template.routing-key.coin_price_response}") String routingKey,
         	@Value("${menu.price_snapshot.name}") String menuName,
            PriceSnapshotCache priceBuffer
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
    public HandlerResult handle(ServiceMessage<PriceSnapshot> serviceMessage) {
        String chatId = serviceMessage.getChatId();
        Integer messageId = serviceMessage.getMessageId();

        PriceSnapshot snapshot = serviceMessage.getData();
        priceBuffer.save(chatId, snapshot);

        EditMessageText answerMessage = createAnswerMessage(snapshot, chatId, messageId);
        return new HandlerResult.Success(answerMessage);
    }

    private EditMessageText createAnswerMessage(PriceSnapshot snapshot, String chatId, Integer messageId) {
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
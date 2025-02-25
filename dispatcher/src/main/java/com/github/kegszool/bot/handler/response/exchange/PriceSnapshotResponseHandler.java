package com.github.kegszool.bot.handler.response.exchange;

import com.github.kegszool.bot.handler.response.BaseResponseHandler;
import com.github.kegszool.bot.handler.result.HandlerResult;
import com.github.kegszool.bot.menu.service.price_snapshot.PriceSnapshotRepository;
import com.github.kegszool.messaging.dto.command_entity.CoinPriceSnapshot;
import com.github.kegszool.messaging.dto.service.ServiceMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class PriceSnapshotResponseHandler extends BaseResponseHandler<CoinPriceSnapshot> {

    @Value("${spring.rabbitmq.template.routing-key.coin_price_response}")
    private String COIN_PRICE_RESPONSE_ROUTING_KEY;

    @Value("${menu.price_snapshot.name}")
    private String PRICE_SNAPSHOT_MENU_NAME;

    private final PriceSnapshotRepository priceSnapshotRepository;

    @Autowired
    public PriceSnapshotResponseHandler(PriceSnapshotRepository priceSnapshotRepository) {
        this.priceSnapshotRepository = priceSnapshotRepository;
    }

    @Override
    public boolean canHandle(String routingKey) {
        return COIN_PRICE_RESPONSE_ROUTING_KEY.equals(routingKey);
    }

    @Override
    public HandlerResult handle(ServiceMessage<CoinPriceSnapshot> serviceMessage) {
        String chatId = serviceMessage.getChatId();
        Integer messageId = serviceMessage.getMessageId();

        CoinPriceSnapshot snapshot = serviceMessage.getData();
        priceSnapshotRepository.saveSnapshot(chatId, snapshot);

        EditMessageText answerMessage = createAnswerMessage(snapshot, chatId, messageId);
        return new HandlerResult.Success(answerMessage);
    }

    private EditMessageText createAnswerMessage(CoinPriceSnapshot snapshot, String chatId, Integer messageId) {
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
package com.github.kegszool.coin.addition.messaging;

import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Value;

import com.github.kegszool.coin.dto.CoinExistenceResult;
import com.github.kegszool.messaging.dto.HandlerResult;
import com.github.kegszool.messaging.dto.service.ServiceMessage;
import com.github.kegszool.messaging.response.BaseResponseHandler;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.util.List;

@Component
public class CoinExistsResponseHandler extends BaseResponseHandler<CoinExistenceResult> {

    private final String responseRoutingKey;
    private final String messageAllCoinsAdded;
    private final String messageSomeCoinsAdded;
    private final String messageNoCoinsAdded;

    private final AddFavoriteCoinRequestProducer requestSender;

    public CoinExistsResponseHandler(
            @Value("${spring.rabbitmq.template.routing-key.check_coin_exists_response}") String responseRoutingKey,
            @Value("${menu.coin_addition.messages.all_coins_added}") String messageAllCoinsAdded,
            @Value("${menu.coin_addition.messages.some_coins_added}") String messageSomeCoinsAdded,
            @Value("${menu.coin_addition.messages.no_coins_added}") String messageNoCoinsAdded,
            AddFavoriteCoinRequestProducer requestSender
    ) {
        this.responseRoutingKey = responseRoutingKey;
        this.messageAllCoinsAdded = messageAllCoinsAdded;
        this.messageSomeCoinsAdded = messageSomeCoinsAdded;
        this.messageNoCoinsAdded = messageNoCoinsAdded;
        this.requestSender = requestSender;
    }

    @Override
    public boolean canHandle(String routingKey) {
        return responseRoutingKey.equals(routingKey);
    }

    @Override
    public HandlerResult handle(ServiceMessage<CoinExistenceResult> serviceMessage) {

        CoinExistenceResult result = serviceMessage.getData();
        List<String> validCoins = result.validCoins();
        List<String> invalidCoins = result.invalidCoins();

        String finalMessage;

        if (!validCoins.isEmpty()) {
            requestSender.send(result.user(), validCoins, serviceMessage.getMessageId(), serviceMessage.getChatId());
        }

        if (!validCoins.isEmpty() && invalidCoins.isEmpty()) {
            finalMessage = messageAllCoinsAdded + String.join(", ", validCoins);

        } else if (!validCoins.isEmpty()) {
            finalMessage = messageSomeCoinsAdded
                    .replace("{validCoins}", String.join(", ", validCoins))
                    .replace("{invalidCoins}", String.join(", ", invalidCoins));

        } else {
            finalMessage = messageNoCoinsAdded;
        }

        SendMessage sendMessage = SendMessage.builder()
                .chatId(serviceMessage.getChatId())
                .text(finalMessage)
                .build();

        return new HandlerResult.Success(sendMessage);
    }
}
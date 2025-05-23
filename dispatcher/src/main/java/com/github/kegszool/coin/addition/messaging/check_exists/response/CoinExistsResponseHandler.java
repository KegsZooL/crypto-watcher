package com.github.kegszool.coin.addition.messaging.check_exists.response;

import java.util.List;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.annotation.Autowired;

import com.github.kegszool.messaging.dto.HandlerResult;
import com.github.kegszool.messaging.dto.service.ServiceMessage;
import com.github.kegszool.messaging.response.BaseResponseHandler;

import com.github.kegszool.coin.dto.CoinExistenceResult;
import com.github.kegszool.localization.LocalizationService;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import com.github.kegszool.coin.addition.messaging.add.AddFavoriteCoinRequestProducer;

@Component
public class CoinExistsResponseHandler extends BaseResponseHandler<CoinExistenceResult> {

    private final String menuName;
    private final String responseRoutingKey;
    private final String allCoinsAddedMsgType;
    private final String someCoinsAddedMsgType;
    private final String noCoinsAddedMsgType;

    private final AddFavoriteCoinRequestProducer requestSender;
    private final LocalizationService localizationService;

    @Autowired
    public CoinExistsResponseHandler(
            @Value("${menu.coin_addition.name}") String menuName,

            @Value("${spring.rabbitmq.template.routing-key.check_coin_exists_response}") String responseRoutingKey,
            @Value("${menu.coin_addition.answer_messages.all_coins_added.msg_type}") String allCoinsAddedMsgType,
            @Value("${menu.coin_addition.answer_messages.some_coins_added.msg_type}") String someCoinsAddedMsgType,
            @Value("${menu.coin_addition.answer_messages.no_coins_added.msg_type}") String noCoinsAddedMsgType,

            AddFavoriteCoinRequestProducer requestSender,
            LocalizationService localizationService
    ) {
        this.menuName = menuName;
        this.responseRoutingKey = responseRoutingKey;
        this.allCoinsAddedMsgType = allCoinsAddedMsgType;
        this.someCoinsAddedMsgType = someCoinsAddedMsgType;
        this.noCoinsAddedMsgType = noCoinsAddedMsgType;
        this.requestSender = requestSender;
        this.localizationService = localizationService;
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
        String chatId = serviceMessage.getChatId();

        if (!validCoins.isEmpty()) {
            requestSender.send(result.user(), validCoins, serviceMessage.getMessageId(), chatId);
        }

        if (!validCoins.isEmpty() && invalidCoins.isEmpty()) {
            String messageAllCoinsAdded = localizationService.getAnswerMessage(menuName, allCoinsAddedMsgType, chatId);
            finalMessage = messageAllCoinsAdded + String.join(", ", validCoins);

        } else if (!validCoins.isEmpty()) {
            String messageSomeCoinsAdded = localizationService.getAnswerMessage(menuName, someCoinsAddedMsgType, chatId);
            finalMessage = messageSomeCoinsAdded
                    .replace("{validCoins}", String.join(", ", validCoins))
                    .replace("{invalidCoins}", String.join(", ", invalidCoins));

        } else {
            finalMessage = localizationService.getAnswerMessage(menuName, noCoinsAddedMsgType, chatId);
        }

        SendMessage sendMessage = SendMessage.builder()
                .chatId(serviceMessage.getChatId())
                .text(finalMessage)
                .build();

        return new HandlerResult.Success(sendMessage);
    }
}
package com.github.kegszool.bot.handler.response.impl;

import com.github.kegszool.bot.handler.response.BaseResponseHandler;
import com.github.kegszool.exception.json.InvalidJsonFormatException;
import com.github.kegszool.exception.json.JsonFieldNotFoundException;
import com.github.kegszool.messaging.dto.ServiceMessage;
import com.github.kegszool.utils.JsonParser;
import com.github.kegszool.utils.KeyboardFactory;
import com.github.kegszool.utils.MessageUtils;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.botapimethods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

//TODO: delete this shit code and edit MessageUtils

@Component
@Log4j2
public class CoinPriceResponseHandler extends BaseResponseHandler {

    @Value("${spring.rabbitmq.template.routing-key.coin_price_response}")
    private String COIN_PRICE_RESPONSE_ROUTING_KEY;

    @Value("${api.exchange.url.market.ticker.response_parameter.last_price}")
    private String LAST_PRICE_FIELD_NAME;

    @Value("${api.exchange.url.market.ticker.response_parameter.coin}")
    private String COIN_FIELD_NAME;

    @Autowired
    public CoinPriceResponseHandler(JsonParser jsonParser, MessageUtils messageUtils) {
        super(jsonParser, messageUtils);
    }

    @Override
    public boolean canHandle(String routingKey) {
        return COIN_PRICE_RESPONSE_ROUTING_KEY.equals(routingKey);
    }

    @Override
    public PartialBotApiMethod<?> handle(ServiceMessage serviceMessage) {

        String response = serviceMessage.getData();
        SendMessage answerMessage;

        try {
            String lastPrice = jsonParser.parse(response, LAST_PRICE_FIELD_NAME);
            String coinName = jsonParser.parse(response, COIN_FIELD_NAME);

            String chatId = serviceMessage.getChatId();
            String title = coinName + " - $" + lastPrice;

            Map<String, String> relationshipBetweenButtonsAndCallbackData = new LinkedHashMap<>();

            relationshipBetweenButtonsAndCallbackData.put("min_24h", "Минимальная цена за 24 часа");
            relationshipBetweenButtonsAndCallbackData.put("max_24h", "Максимальная цена за 24 часа");
            relationshipBetweenButtonsAndCallbackData.put("open_menu", "Меню");

            var keyboard = KeyboardFactory.create(relationshipBetweenButtonsAndCallbackData);
            answerMessage = new SendMessage(chatId, title);
            answerMessage.setReplyMarkup(keyboard);

        } catch (JsonFieldNotFoundException e) {
            throw new RuntimeException(e);
        } catch (InvalidJsonFormatException e) {
            throw new RuntimeException(e);
        }
        return answerMessage;
    }
}
package com.github.kegszool.bot.handler.response.impl;

import com.github.kegszool.messaging.dto.ServiceMessage;
import com.github.kegszool.bot.handler.response.ResponseHandler;
import com.github.kegszool.utils.MessageUtils;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.botapimethods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;


@Component
@Log4j2
public class CoinPriceResponseHandler implements ResponseHandler {

    @Value("${spring.rabbitmq.template.routing-key.coin_price_response_key}")
    private String COIN_PRICE_RESPONSE_ROUTING_KEY;

    private final MessageUtils messageUtils;

    @Autowired
    public CoinPriceResponseHandler(MessageUtils messageUtils) {
        this.messageUtils = messageUtils;
    }

    @Override
    public boolean canHandle(String routingKey) {
        return COIN_PRICE_RESPONSE_ROUTING_KEY.equals(routingKey);
    }

    @Override
    public PartialBotApiMethod<?> handle(ServiceMessage serviceMessage) {
        var answerMessage = new SendMessage(serviceMessage.getChatId(), serviceMessage.getData());
        return answerMessage;
    }
}
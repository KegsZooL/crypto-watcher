package com.github.kegszool.response_handler.impl;

import com.github.kegszool.response_handler.ResponseHandler;
import com.github.kegszool.utils.MessageUtils;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.botapimethods.PartialBotApiMethod;

//TODO: добавить логирование + разобраться с получением chatID;

@Component
@Log4j2
public class CoinPriceResponseHandler implements ResponseHandler {

    private final MessageUtils messageUtils;

    @Value("${spring.rabbitmq.template.routing-key.coin_price_key}")
    private String COIN_PRICE_ROUTING_KEY;

    @Autowired
    public CoinPriceResponseHandler(MessageUtils messageUtils) {
        this.messageUtils = messageUtils;
    }

    @Override
    public boolean canHandle(String routingKey) {
        return COIN_PRICE_ROUTING_KEY.equals(routingKey);
    }

    @Override
    public PartialBotApiMethod<?> handle(String response) {
//        return messageUtils.createSendMessageByText("");
        return null;
    }
}
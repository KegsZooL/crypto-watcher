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

//TODO: добавить логирование + разобраться с получением chatID;

@Component
@Log4j2
public class CoinPriceResponseHandler implements ResponseHandler {

    private final MessageUtils messageUtils;

    @Value("${spring.rabbitmq.template.routing-key.coin_price_response_key}")
    private String COIN_PRICE_RESPONSE_ROUTING_KEY;

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
        SendMessage answerMessage = new SendMessage(serviceMessage.getChatId().toString(), serviceMessage.getData());
//        return messageUtils.createSendMessageByText("");
        return answerMessage;
    }
}
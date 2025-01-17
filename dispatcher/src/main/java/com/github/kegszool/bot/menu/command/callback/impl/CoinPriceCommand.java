package com.github.kegszool.bot.menu.command.callback.impl;

import com.github.kegszool.messaging.dto.ServiceMessage;
import com.github.kegszool.messaging.producer.RequestProducerService;
import com.github.kegszool.bot.menu.command.callback.CallbackCommand;
import com.github.kegszool.utils.MessageUtils;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.botapimethods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

@Component
@Log4j2
public class CoinPriceCommand extends CallbackCommand {

    @Value("${coin.prefix}")
    private String COIN_PREFIX;

    @Value("${spring.rabbitmq.template.routing-key.coin_price_request}")
    private String COIN_PRICE_SNAPSHOT_REQUEST_ROUTING_KEY;

    private final MessageUtils messageUtils;
    private final RequestProducerService requestService;

    @Autowired
    public CoinPriceCommand(
            MessageUtils messageUtils,
            RequestProducerService requestService
    ) {
        this.messageUtils = messageUtils;
        this.requestService = requestService;
    }

    @Override
    protected boolean canHandleCommand(String command) {
        return command.startsWith(COIN_PREFIX);
    }

    @Override
    protected PartialBotApiMethod<?> handleCommand(CallbackQuery query) {
        String coinName = getCoinNameWithoutPrefix(query);
        produceRequest(query, coinName);
        var answerMessage = createAnswerMessage(query, coinName);
        return answerMessage;
    }

    private String getCoinNameWithoutPrefix(CallbackQuery query) {
        String data = query.getData();
        return data.substring(COIN_PREFIX.length());
    }

    private void produceRequest(CallbackQuery query, String coinName) {
        String chatId = messageUtils.extractChatId(query);

        var serviceMessage = new ServiceMessage();
        serviceMessage.setData(coinName);
        serviceMessage.setChatId(chatId);

        requestService.produce(COIN_PRICE_SNAPSHOT_REQUEST_ROUTING_KEY, serviceMessage);
        log.info("A request has been sent to receive the price of the '{}' coin", coinName);
    }

    private EditMessageText createAnswerMessage(CallbackQuery query, String coinName) {
        String text = "Вы выбрали монету: " + coinName;
        return messageUtils.createEditMessage(query, text);
    }
}
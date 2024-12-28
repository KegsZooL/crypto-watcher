package com.github.kegszool.menu.callback_handler.impl;

import com.github.kegszool.communication_service.RequestProducerService;
import com.github.kegszool.menu.callback_handler.CallbackHandler;
import com.github.kegszool.utils.MessageUtils;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

@Component
@Log4j2
public class CoinPriceRequestCallbackHandler implements CallbackHandler {

    private final RequestProducerService requestService;
    private final MessageUtils messageUtils;

    @Value("${coin.prefix}")
    private String COIN_PREFIX;

    @Value("${spring.rabbitmq.queues.request_queue}")
    private String COIN_REQUEST_QUEUE;

    @Autowired
    public CoinPriceRequestCallbackHandler(
            RequestProducerService requestService,
            MessageUtils messageUtils
    ) {
        this.requestService = requestService;
        this.messageUtils = messageUtils;
    }

    @Override
    public boolean canHandle(String command) {
        return command.startsWith(COIN_PREFIX);
    }

    @Override
    public EditMessageText handle(CallbackQuery query) {
        String cryptocurrencyName = getCryptocurrencyNameByCallbackData(query);
        log.info("The process of getting the price of a coin: '{}'", cryptocurrencyName);
        requestService.produce(COIN_REQUEST_QUEUE, cryptocurrencyName);
        String text = String.format("Вы выбрали монету: %s", cryptocurrencyName);
        return messageUtils.createEditMessageText(query, text);
    }

    private String getCryptocurrencyNameByCallbackData(CallbackQuery query) {
        String data = query.getData();
        int lengthOfCoinPrefix = COIN_PREFIX.length();
        int lengthOfData = data.length();
        return data.substring(lengthOfCoinPrefix, lengthOfData);
    }
}

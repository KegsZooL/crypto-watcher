package com.github.kegszool.menu.command;

import com.github.kegszool.DTO.DataTransferObject;
import com.github.kegszool.communication_service.RequestProducerService;
import com.github.kegszool.utils.MessageUtils;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.botapimethods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

@Component
@Log4j2
public class CoinPriceCommand extends CallbackCommand {

    private final RequestProducerService requestService;
    private final MessageUtils messageUtils;

    @Value("${coin.prefix}")
    private String COIN_PREFIX;

    @Value("${spring.rabbitmq.template.routing-key.coin_price_request_key}")
    private String COIN_PRICE_REQUEST_ROUTING_KEY;

    public CoinPriceCommand(
            RequestProducerService requestService,
            MessageUtils messageUtils
    ) {
        this.requestService = requestService;
        this.messageUtils = messageUtils;
    }

    @Override
    protected boolean canHandleCommand(String command) {
        return command.startsWith(COIN_PREFIX);
    }

    @Override
    protected PartialBotApiMethod<?> handleCommand(CallbackQuery query) {
        String cryptocurrencyName = getCryptocurrencyNameByCallbackData(query);
        log.info("The process of getting the price of a coin: '{}'", cryptocurrencyName);

        Long chatId = query.getMessage().getChatId();
        var dataTransferObject = new DataTransferObject();
        dataTransferObject.setData(cryptocurrencyName);
        dataTransferObject.setChatId(chatId);
        requestService.produce(COIN_PRICE_REQUEST_ROUTING_KEY, dataTransferObject);

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

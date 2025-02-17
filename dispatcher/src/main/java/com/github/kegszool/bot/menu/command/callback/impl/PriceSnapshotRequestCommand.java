package com.github.kegszool.bot.menu.command.callback.impl;

import com.github.kegszool.utils.MessageUtils;
import com.github.kegszool.bot.menu.command.callback.CallbackCommand;
import com.github.kegszool.exception.bot.data.ParsingCoinDataException;
import com.github.kegszool.messaging.dto.service.ServiceMessage;
import com.github.kegszool.messaging.producer.RequestProducerService;

import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.annotation.Autowired;

import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.methods.botapimethods.PartialBotApiMethod;

import lombok.extern.log4j.Log4j2;

@Log4j2
@Component
public class PriceSnapshotRequestCommand extends CallbackCommand {

    @Value("${menu.coin_selection.prefix[0].coin}")
    private String COIN_PREFIX;

    @Value("${menu.coin_selection.prefix[1].currency}")
    private String CURRENCY_PREFIX;

    @Value("${spring.rabbitmq.template.routing-key.coin_price_request}")
    private String COIN_PRICE_REQUEST_ROUTING_KEY;

    @Value("${menu.coin_selection.answer_message}")
    private String ANSWER_MESSAGE_TEXT;

    private final MessageUtils messageUtils;
    private final RequestProducerService requestService;

    @Autowired
    public PriceSnapshotRequestCommand(
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
        String coinData = query.getData();
        String coinNameWithCurrencyPrefix = extractCoinNameWithCurrency(coinData);
        String coinNameWithoutPrefixes = extractCoinNameWithoutPrefixes(coinNameWithCurrencyPrefix);

        var answerMessage = createAnswerMessage(query, coinNameWithoutPrefixes);
        produceRequest(query, coinNameWithCurrencyPrefix, answerMessage.getMessageId());

        return answerMessage;
    }

    private String extractCoinNameWithCurrency(String coinData) {
        int beginIndex = COIN_PREFIX.length();
        int endIndex = coinData.length();

        if (beginIndex > endIndex || beginIndex == 0) {
            throw handleInvalidIndicesException(coinData, beginIndex, endIndex);
        }
        return coinData.substring(beginIndex, endIndex);
    }

    private String extractCoinNameWithoutPrefixes(String coinNameWithCurrencyPrefix) {
        int beginIndex = 0;
        int endIndex = coinNameWithCurrencyPrefix.length() - CURRENCY_PREFIX.length();

        if (endIndex < 0) {
            throw handleInvalidIndicesException(coinNameWithCurrencyPrefix, beginIndex, endIndex);
        }
        return coinNameWithCurrencyPrefix.substring(beginIndex, endIndex);
    }

    private ParsingCoinDataException handleInvalidIndicesException(
            String data, int beginIndex, int endIndex
    ) {
        String errorMessage = String.format(
                "Invalid indices for data \"%s\", Begin index: %d, End index: %d",
                data, beginIndex, endIndex
        );
        log.error(errorMessage);
        return new ParsingCoinDataException(errorMessage);
    }

    private EditMessageText createAnswerMessage(CallbackQuery query, String coinNameWithCurrencyPrefix) {
        String text = ANSWER_MESSAGE_TEXT + String.format(" — <b>%s</b>", coinNameWithCurrencyPrefix);
        var answerMessage = messageUtils.createEditMessage(query, text);
        answerMessage.setParseMode(ParseMode.HTML);
        return answerMessage;
    }

    private void produceRequest(CallbackQuery query, String coinNameWithCurrencyPrefix, Integer messageId) {
        String chatId = messageUtils.extractChatId(query);
        ServiceMessage<String> serviceMessage = new ServiceMessage(
                messageId, chatId, coinNameWithCurrencyPrefix
        );
        requestService.produce(COIN_PRICE_REQUEST_ROUTING_KEY, serviceMessage);
        log.info("A request has been sent to receive the price snapshot of the '{}' coin", coinNameWithCurrencyPrefix);
    }
}

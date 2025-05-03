package com.github.kegszool.coin.price.command;

import com.github.kegszool.localization.LocalizationService;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.annotation.Autowired;

import com.github.kegszool.messaging.dto.service.ServiceMessage;
import com.github.kegszool.messaging.producer.RequestProducerService;

import com.github.kegszool.command.callback.CallbackCommand;
import com.github.kegszool.coin.exception.ParsingCoinDataException;

import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.methods.botapimethods.PartialBotApiMethod;

import lombok.extern.log4j.Log4j2;

@Log4j2
@Component
public class PriceRequestCommand extends CallbackCommand {

    private final String coinPrefix;
    private final String currencyPrefix;
    private final String routingKey;
    private final String menuName;
    private final LocalizationService localizationService;
    private final RequestProducerService requestService;

    @Autowired
    public PriceRequestCommand(
            @Value("${menu.coin_selection.prefix.coin}") String coinPrefix,
            @Value("${menu.coin_selection.prefix.currency}") String currencyPrefix,
            @Value("${spring.rabbitmq.template.routing-key.coin_price_request}") String routingKey,
            @Value("${menu.coin_selection.name}") String menuName,
            RequestProducerService requestService,
            LocalizationService localizationService
    ) {
        this.coinPrefix = coinPrefix;
        this.currencyPrefix = currencyPrefix;
        this.routingKey = routingKey;
        this.menuName = menuName;
        this.requestService = requestService;
        this.localizationService = localizationService;
    }

    @Override
    protected boolean canHandleCommand(String command) {
        return command.startsWith(coinPrefix);
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
        int beginIndex = coinPrefix.length();
        int endIndex = coinData.length();

        if (beginIndex > endIndex || beginIndex == 0) {
            throw handleInvalidIndicesException(coinData, beginIndex, endIndex);
        }
        return coinData.substring(beginIndex, endIndex);
    }

    private String extractCoinNameWithoutPrefixes(String coinNameWithCurrencyPrefix) {
        int beginIndex = 0;
        int endIndex = coinNameWithCurrencyPrefix.length() - currencyPrefix.length();

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
        String answerMsg = localizationService.getAnswerMessage(menuName, messageUtils.extractChatId(query));
        String prettyMsg = answerMsg + String.format(" — <b>%s</b>", coinNameWithCurrencyPrefix);
        EditMessageText answerMessage = messageUtils.createEditMessage(query, prettyMsg);
        answerMessage.setParseMode(ParseMode.HTML);
        return answerMessage;
    }

    private void produceRequest(CallbackQuery query, String coinNameWithCurrencyPrefix, Integer messageId) {
        String chatId = messageUtils.extractChatId(query);
        ServiceMessage<String> serviceMessage = new ServiceMessage<>(
                messageId, chatId, coinNameWithCurrencyPrefix
        );
        requestService.produce(routingKey, serviceMessage);
        log.info("A request has been sent to receive the price snapshot of the '{}' coin", coinNameWithCurrencyPrefix);
    }
}

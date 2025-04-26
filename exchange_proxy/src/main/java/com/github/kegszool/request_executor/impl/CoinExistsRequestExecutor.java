package com.github.kegszool.request_executor.impl;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.annotation.Autowired;

import com.github.kegszool.messaging.dto.UserDto;
import com.github.kegszool.messaging.dto.command_entity.UserCoinData;
import com.github.kegszool.exception.request.CheckingCoinException;
import com.github.kegszool.exception.json.JsonFieldNotFoundException;
import com.github.kegszool.exception.json.InvalidJsonFormatException;
import com.github.kegszool.messaging.dto.command_entity.CoinExistenceResult;

import java.util.ArrayList;
import java.util.List;
import com.github.kegszool.utils.JsonParser;
import com.github.kegszool.rest.RestCryptoController;
import com.github.kegszool.messaging.dto.service.ServiceMessage;
import com.github.kegszool.request_executor.BaseRequestExecutor;
import org.springframework.web.client.RestClientException;

@Log4j2
@Component
public class CoinExistsRequestExecutor extends BaseRequestExecutor<UserCoinData, CoinExistenceResult> {

    private final static String SUCCESS_RESPONSE_CODE = "0";
    private final String responseRoutingKey;
    private final String requestUrl;
    private final String currencySuffix;

    @Autowired
    public CoinExistsRequestExecutor(
            RestCryptoController restCryptoController,
            JsonParser jsonParser,

            @Value("${spring.rabbitmq.template.routing-key.check_coin_exists_response}")
            String responseRoutingKey,

            @Value("${api.exchange.url.market.instruments}") String requestUrl,
            @Value("${currency_suffix}") String currencySuffix
    ) {
        super(restCryptoController, jsonParser);
        this.responseRoutingKey = responseRoutingKey;
        this.requestUrl = requestUrl;
        this.currencySuffix = currencySuffix;
    }

    @Override
    public String getResponseRoutingKey() {
        return responseRoutingKey;
    }

    @Override
    public ServiceMessage<CoinExistenceResult> execute(ServiceMessage<UserCoinData> serviceMessage) {

        UserCoinData userCoinData = serviceMessage.getData();

        List<String> coins = userCoinData.getCoinsToAdd();
        List<String> validCoins = new ArrayList<>();
        List<String> invalidCoins = new ArrayList<>();

        for (String coin : coins) {
            String urlWithData = requestUrl + coin + currencySuffix;
            try {
                String response = restCryptoController.getResponse(urlWithData);
                String code = jsonParser.parse(response, "code");

                String coinWithoutCurrencySuffix = coin.replace(currencySuffix, "");
                if (SUCCESS_RESPONSE_CODE.equals(code)) {
                    validCoins.add(coinWithoutCurrencySuffix);
                } else {
                    invalidCoins.add(coinWithoutCurrencySuffix);
                }

            } catch (RestClientException | JsonFieldNotFoundException | InvalidJsonFormatException ex) {
                throw createException(coin);
            }
        }
        return createResponseMessage(validCoins, invalidCoins, serviceMessage);
    }

    private ServiceMessage<CoinExistenceResult> createResponseMessage(
            List<String> validCoins, List<String> invalidCoins, ServiceMessage<UserCoinData> serviceMessage
    ) {

        UserDto userDto = serviceMessage.getData().getUser();
        CoinExistenceResult result = new CoinExistenceResult(validCoins, invalidCoins, userDto);

        ServiceMessage<CoinExistenceResult> responseMessage = new ServiceMessage<>();
        responseMessage.setData(result);
        responseMessage.setMessageId(serviceMessage.getMessageId());
        responseMessage.setChatId(serviceMessage.getChatId());

        return responseMessage;
    }

    private CheckingCoinException createException(String coin) {
        log.error("Error checking the coin - '{}'", coin);
        return new CheckingCoinException(String.format("Coin - '%s'", coin));
    }
}
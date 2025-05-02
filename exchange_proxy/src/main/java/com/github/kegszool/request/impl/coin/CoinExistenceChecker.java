package com.github.kegszool.request.impl.coin;

import com.github.kegszool.exception.json.InvalidJsonFormatException;
import com.github.kegszool.exception.json.JsonFieldNotFoundException;
import com.github.kegszool.exception.request.CheckingCoinException;
import com.github.kegszool.rest.RestCryptoController;
import com.github.kegszool.utils.JsonParser;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;

@Log4j2
@Component
public class CoinExistenceChecker {

    private static final String SUCCESS_RESPONSE_CODE = "0";

    private final RestCryptoController restCryptoController;
    private final JsonParser jsonParser;
    private final String requestUrl;
    private final String currencySuffix;

    @Autowired
    public CoinExistenceChecker(
            RestCryptoController restCryptoController,
            JsonParser jsonParser,
            @Value("${api.exchange.url.market.instruments}") String requestUrl,
            @Value("${currency_suffix}") String currencySuffix
    ) {
        this.restCryptoController = restCryptoController;
        this.jsonParser = jsonParser;
        this.requestUrl = requestUrl;
        this.currencySuffix = currencySuffix;
    }

    public boolean exists(String coinName) {
        String urlWithData = requestUrl + coinName + currencySuffix;

        try {
            String response = restCryptoController.getResponse(urlWithData);
            String code = jsonParser.parse(response, "code");
            return SUCCESS_RESPONSE_CODE.equals(code);
        } catch (RestClientException | JsonFieldNotFoundException | InvalidJsonFormatException ex) {
            throw createException(coinName);
        }
    }

    private CheckingCoinException createException(String coin) {
        log.error("Error checking the coin - '{}'", coin);
        return new CheckingCoinException(String.format("Coin - '%s'", coin));
    }
}
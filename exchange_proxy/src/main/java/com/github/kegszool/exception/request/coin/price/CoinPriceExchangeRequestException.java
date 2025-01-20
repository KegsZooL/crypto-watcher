package com.github.kegszool.exception.request.coin.price;

import com.github.kegszool.exception.request.ExchangeRequestException;

public class CoinPriceExchangeRequestException extends ExchangeRequestException {

    public CoinPriceExchangeRequestException(String msg) {
        super(msg);
    }

    public CoinPriceExchangeRequestException(String msg, Throwable ex) { super(msg, ex); }
}

package com.github.kegszool.exception.exchange.request.handler;

import com.github.kegszool.exception.exchange.request.ExchangeRequestException;

public class ExchangeRequestHandlerNotFoundException extends ExchangeRequestException {

    public ExchangeRequestHandlerNotFoundException(String msg) {
        super(msg);
    }
}
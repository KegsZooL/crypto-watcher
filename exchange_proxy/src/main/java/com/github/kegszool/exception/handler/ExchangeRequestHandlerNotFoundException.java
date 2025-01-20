package com.github.kegszool.exception.handler;

import com.github.kegszool.exception.request.ExchangeRequestException;

public class ExchangeRequestHandlerNotFoundException extends ExchangeRequestException {

    public ExchangeRequestHandlerNotFoundException(String msg) {
        super(msg);
    }
}
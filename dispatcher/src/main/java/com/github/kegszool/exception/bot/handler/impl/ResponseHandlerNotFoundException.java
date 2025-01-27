package com.github.kegszool.exception.bot.handler.impl;

import com.github.kegszool.exception.bot.handler.HandlerNotFoundException;

public class ResponseHandlerNotFoundException extends HandlerNotFoundException {

    public ResponseHandlerNotFoundException(String message) {
        super(message);
    }
}

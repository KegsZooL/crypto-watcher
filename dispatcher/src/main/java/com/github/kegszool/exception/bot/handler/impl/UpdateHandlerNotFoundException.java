package com.github.kegszool.exception.bot.handler.impl;

import com.github.kegszool.exception.bot.handler.HandlerNotFoundException;

public class UpdateHandlerNotFoundException extends HandlerNotFoundException {
    public UpdateHandlerNotFoundException(String message) {
        super(message);
    }
}

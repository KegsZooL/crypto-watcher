package com.github.kegszool.exception.bot.handler;

public abstract class HandlerNotFoundException extends RuntimeException {

    protected HandlerNotFoundException(String message) {
        super(message);
    }
}

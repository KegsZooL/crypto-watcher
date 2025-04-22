package com.github.kegszool.bot.exception.method;

public class TelegramMethodException extends RuntimeException {

    public TelegramMethodException(String message) {
        super(message);
    }
}
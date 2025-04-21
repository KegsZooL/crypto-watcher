package com.github.kegszool.bot.exception.method;

public class UnsupportedTelegramMethodTypeException extends TelegramMethodExecutionException {

    public UnsupportedTelegramMethodTypeException(String message) {
        super(message);
    }
}
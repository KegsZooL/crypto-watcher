package com.github.kegszool.exception.bot.data.method;

public class MethodException extends RuntimeException {

    public MethodException(String message) {
        super(message);
    }

    public MethodException(String message, Throwable cause) {
        super(message, cause);
    }
}

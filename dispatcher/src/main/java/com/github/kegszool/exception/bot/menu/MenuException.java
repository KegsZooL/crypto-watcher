package com.github.kegszool.exception.bot.menu;

public class MenuException extends Exception {

    public MenuException(String message, Throwable cause) {
        super(message, cause);
    }

    public MenuException(String message) {
        super(message);
    }
}

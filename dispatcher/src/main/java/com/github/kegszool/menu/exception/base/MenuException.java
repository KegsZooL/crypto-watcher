package com.github.kegszool.menu.exception.base;

public class MenuException extends RuntimeException {

    public MenuException(String message, Throwable cause) {
        super(message, cause);
    }

    public MenuException(String message) {
        super(message);
    }
}
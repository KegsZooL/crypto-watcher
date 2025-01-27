package com.github.kegszool.exception.bot.menu.configuration;

import com.github.kegszool.exception.bot.menu.MenuException;

public class MenuConfigurationException extends MenuException {

    public MenuConfigurationException(String message) {
        super(message);
    }

    public MenuConfigurationException(String message, Throwable cause) {
        super(message, cause);
    }
}

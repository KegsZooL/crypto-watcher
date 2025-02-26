package com.github.kegszool.exception.bot.menu.configuration.section;

import com.github.kegszool.exception.bot.menu.configuration.MenuConfigurationException;

public class InvalidMenuSectionConfigException extends MenuConfigurationException {

    public InvalidMenuSectionConfigException(String message) {
        super(message);
    }

    public InvalidMenuSectionConfigException(String message, Throwable cause) {
        super(message, cause);
    }
}
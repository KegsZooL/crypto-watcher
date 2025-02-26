package com.github.kegszool.exception.bot.menu.configuration.section.transfer;

import com.github.kegszool.exception.bot.menu.configuration.MenuConfigurationException;


public class IllegalTransferredCallbackDataException extends MenuConfigurationException {

    public IllegalTransferredCallbackDataException(String message) {
        super(message);
    }
}
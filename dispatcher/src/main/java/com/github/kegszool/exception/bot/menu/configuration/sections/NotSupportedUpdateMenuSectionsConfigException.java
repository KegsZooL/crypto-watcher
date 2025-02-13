package com.github.kegszool.exception.bot.menu.configuration.sections;

import com.github.kegszool.exception.bot.menu.configuration.MenuConfigurationException;

public class NotSupportedUpdateMenuSectionsConfigException extends MenuConfigurationException {

    public NotSupportedUpdateMenuSectionsConfigException(String menuName) {
        super("Menu:" + menuName);
    }
}
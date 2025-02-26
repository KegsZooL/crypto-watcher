package com.github.kegszool.exception.bot.menu.selection;

import com.github.kegszool.exception.bot.menu.MenuException;

public class SelectionButtonNotFoundException extends MenuException {

    public SelectionButtonNotFoundException(String message) {
        super(message);
    }
}
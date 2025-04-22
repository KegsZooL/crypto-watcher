package com.github.kegszool.menu.exception.format;

import com.github.kegszool.menu.exception.section.MenuSectionParsingException;

public class MenuKeyValueFormatException extends MenuSectionParsingException {

    public MenuKeyValueFormatException(String message) {
        super(message);
    }
}
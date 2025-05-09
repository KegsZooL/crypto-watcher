package com.github.kegszool.menu.exception.section;

public class MenuCommandDescriptionNotFoundException extends RuntimeException {
    public MenuCommandDescriptionNotFoundException(String command) {
        super("Command: " + command);
    }
}
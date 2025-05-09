package com.github.kegszool.localization.exception;

public class MenuLocalizationNotFoundException extends RuntimeException {

    public MenuLocalizationNotFoundException(String key) {
        super("Key: " + key);
    }
}
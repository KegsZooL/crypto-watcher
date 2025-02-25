package com.github.kegszool.exception.bot;

public class MenuSectionTransferNotFoundException extends RuntimeException {
    public MenuSectionTransferNotFoundException(String nonExistentTransferClassName) {
        super(String.format("Non-existent transfer: \"%s\"", nonExistentTransferClassName));
    }
}
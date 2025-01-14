package com.github.kegszool.exception.handler.impl;

import com.github.kegszool.exception.handler.HandlerNotFoundException;

public class UpdateHandlerNotFoundException extends HandlerNotFoundException {
    public UpdateHandlerNotFoundException(String message) {
        super(message);
    }
}

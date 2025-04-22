package com.github.kegszool.update.exception;

import com.github.kegszool.router.HandlerNotFoundException;

public class UpdateHandlerNotFoundException extends HandlerNotFoundException {

    public UpdateHandlerNotFoundException(String message) {
        super(message);
    }
}
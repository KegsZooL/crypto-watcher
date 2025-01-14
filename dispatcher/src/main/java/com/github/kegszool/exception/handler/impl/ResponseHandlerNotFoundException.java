package com.github.kegszool.exception.handler.impl;

import com.github.kegszool.exception.handler.HandlerNotFoundException;

public class ResponseHandlerNotFoundException extends HandlerNotFoundException {

    public ResponseHandlerNotFoundException(String message) {
        super(message);
    }
}

package com.github.kegszool.messaging.response.exception;

import com.github.kegszool.router.HandlerNotFoundException;

public class ResponseHandlerNotFoundException extends HandlerNotFoundException {

    public ResponseHandlerNotFoundException(String message) {
        super(message);
    }
}

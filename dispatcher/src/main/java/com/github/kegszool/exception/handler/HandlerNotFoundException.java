package com.github.kegszool.exception.handler;

public abstract class HandlerNotFoundException extends RuntimeException {

    protected HandlerNotFoundException(String message) {
        super(message);
    }
}

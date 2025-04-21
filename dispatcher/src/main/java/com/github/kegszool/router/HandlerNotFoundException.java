package com.github.kegszool.router;

public abstract class HandlerNotFoundException extends RuntimeException {

    protected HandlerNotFoundException(String message) {
        super(message);
    }
}
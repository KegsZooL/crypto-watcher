package com.github.kegszool.exception;

public class ResponseHandlerNotFoundException extends RuntimeException {

    public ResponseHandlerNotFoundException(String message) {
        super(message);
    }
}

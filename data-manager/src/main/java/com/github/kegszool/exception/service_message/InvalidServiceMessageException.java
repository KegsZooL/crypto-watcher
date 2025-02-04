package com.github.kegszool.exception.service_message;

public class InvalidServiceMessageException extends RuntimeException {

    public InvalidServiceMessageException(String message) {
        super(message);
    }

    public InvalidServiceMessageException(String message, Throwable cause) {
        super(message, cause);
    }
}

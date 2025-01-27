package com.github.kegszool.exception.service_message;

public class ServiceMessageSendingException extends RuntimeException {

    public ServiceMessageSendingException(String message, Throwable cause) {
        super(message, cause);
    }
}

package com.github.kegszool.messaging.service_message.exception;

public class ServiceMessageSendingException extends RuntimeException {

    public ServiceMessageSendingException(String message, Throwable cause) {
        super(message, cause);
    }
}
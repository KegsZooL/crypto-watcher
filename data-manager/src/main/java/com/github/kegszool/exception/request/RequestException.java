package com.github.kegszool.exception.request;

public class RequestException extends RuntimeException {

    public RequestException(String msg) {
        super(msg);
    }

    public RequestException(String msg, Throwable ex) { super(msg, ex); }
}
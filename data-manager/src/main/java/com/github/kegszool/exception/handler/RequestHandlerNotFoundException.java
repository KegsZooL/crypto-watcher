package com.github.kegszool.exception.handler;

import com.github.kegszool.exception.request.RequestException;

public class RequestHandlerNotFoundException extends RequestException {

    public RequestHandlerNotFoundException(String msg) {
        super(msg);
    }
}
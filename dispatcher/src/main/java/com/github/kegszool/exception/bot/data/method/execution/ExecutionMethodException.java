package com.github.kegszool.exception.bot.data.method.execution;

import com.github.kegszool.exception.bot.data.method.MethodException;

public class ExecutionMethodException extends MethodException {

    public ExecutionMethodException(String message) {
        super(message);
    }

    public ExecutionMethodException(String message, Throwable cause) {
        super(message, cause);
    }
}

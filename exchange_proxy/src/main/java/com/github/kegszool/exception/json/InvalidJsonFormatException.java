package com.github.kegszool.exception.json;

import java.io.IOException;

public class InvalidJsonFormatException extends IOException {
    public InvalidJsonFormatException(String message) {
        super(message);
    }
}
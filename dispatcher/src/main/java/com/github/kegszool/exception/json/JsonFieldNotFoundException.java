package com.github.kegszool.exception.json;

import java.io.IOException;

public class JsonFieldNotFoundException extends IOException {

    public JsonFieldNotFoundException(String message) {
        super(message);
    }
}
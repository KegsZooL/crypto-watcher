package com.github.kegszool.messaging.executor;

import com.github.kegszool.utils.JsonParser;
import com.github.kegszool.rest.RestCryptoController;

public abstract class BaseRequestExecutor<I, O> implements RequestExecutor<I, O> {
    protected final RestCryptoController restCryptoController;
    protected final JsonParser jsonParser;

    public BaseRequestExecutor(RestCryptoController restCryptoController, JsonParser jsonParser) {
        this.restCryptoController = restCryptoController;
        this.jsonParser = jsonParser;
    }
    public abstract String getResponseRoutingKey();
}
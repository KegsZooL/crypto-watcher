package com.github.kegszool.request_executor;

import com.github.kegszool.rest.RestCryptoController;
import com.github.kegszool.utils.JsonParser;

public abstract class BaseRequestExecutor<I, O> implements RequestExecutor<I, O> {
    protected final RestCryptoController restCryptoController;
    protected final JsonParser jsonParser;

    public BaseRequestExecutor(RestCryptoController restCryptoController, JsonParser jsonParser) {
        this.restCryptoController = restCryptoController;
        this.jsonParser = jsonParser;
    }
    public abstract String getResponseRoutingKey();
}
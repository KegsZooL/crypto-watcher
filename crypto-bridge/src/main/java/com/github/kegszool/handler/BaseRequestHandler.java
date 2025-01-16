package com.github.kegszool.handler;

import com.github.kegszool.controller.RestCryptoController;
import com.github.kegszool.utils.JsonParser;

public abstract class BaseRequestHandler  implements RequestHandler {

    protected final RestCryptoController restCryptoController;
    protected final JsonParser jsonParser;

    public BaseRequestHandler(RestCryptoController restCryptoController, JsonParser jsonParser) {
        this.restCryptoController = restCryptoController;
        this.jsonParser = jsonParser;
    }
}

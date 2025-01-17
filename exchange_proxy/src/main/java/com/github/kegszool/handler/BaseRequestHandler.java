package com.github.kegszool.handler;

import com.github.kegszool.controller.RestCryptoController;

public abstract class BaseRequestHandler  implements RequestHandler {

    protected final RestCryptoController restCryptoController;

    public BaseRequestHandler(RestCryptoController restCryptoController) {
        this.restCryptoController = restCryptoController;
    }
}

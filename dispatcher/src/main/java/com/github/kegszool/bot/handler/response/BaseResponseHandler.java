package com.github.kegszool.bot.handler.response;

import com.github.kegszool.utils.JsonParser;
import com.github.kegszool.utils.MessageUtils;

public abstract class BaseResponseHandler implements ResponseHandler{

    protected final JsonParser jsonParser;
    protected final MessageUtils messageUtils;

    public BaseResponseHandler(JsonParser jsonParser, MessageUtils messageUtils) {
        this.jsonParser = jsonParser;
        this.messageUtils = messageUtils;
    }
}

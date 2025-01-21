package com.github.kegszool.bot.handler.response;

import com.github.kegszool.utils.MessageUtils;

public abstract class BaseResponseHandler implements ResponseHandler{

    protected final MessageUtils messageUtils;

    public BaseResponseHandler(MessageUtils messageUtils) {
        this.messageUtils = messageUtils;
    }
}

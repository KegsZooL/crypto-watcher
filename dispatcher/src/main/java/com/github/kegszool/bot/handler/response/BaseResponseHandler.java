package com.github.kegszool.bot.handler.response;

import com.github.kegszool.utils.MessageUtils;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class BaseResponseHandler<T> implements ResponseHandler<T>{

    @Autowired
    protected MessageUtils messageUtils;
}
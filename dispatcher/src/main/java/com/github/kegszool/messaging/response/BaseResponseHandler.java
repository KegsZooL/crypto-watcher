package com.github.kegszool.messaging.response;

import com.github.kegszool.messaging.util.MessageUtils;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class BaseResponseHandler<T> implements ResponseHandler<T> {

    @Autowired
    protected MessageUtils messageUtils;
}
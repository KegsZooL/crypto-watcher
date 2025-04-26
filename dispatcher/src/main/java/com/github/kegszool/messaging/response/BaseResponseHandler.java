package com.github.kegszool.messaging.response;

import com.github.kegszool.messaging.util.MessageUtils;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class BaseResponseHandler<I> implements ResponseHandler<I> {

    @Autowired
    protected MessageUtils messageUtils;
}
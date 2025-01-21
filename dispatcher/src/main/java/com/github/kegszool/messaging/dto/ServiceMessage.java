package com.github.kegszool.messaging.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ServiceMessage<T> {
    private String chatId;
    private T data;
}
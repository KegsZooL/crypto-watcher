package com.github.kegszool.messaging.dto.service;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ServiceMessage<T> {
    private Integer messageId;
    private String chatId;
    private T data;
}
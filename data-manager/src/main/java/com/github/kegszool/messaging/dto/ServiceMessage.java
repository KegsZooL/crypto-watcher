package com.github.kegszool.messaging.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ServiceMessage<T> {
    private Integer messageId;
    private String chatId;
    private T data;
}
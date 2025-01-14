package com.github.kegszool.messaging.dto;

public class ServiceMessage {

    public String data;
    public Long chatId;

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public Long getChatId() {
        return chatId;
    }

    public void setChatId(Long chatId) {
        this.chatId = chatId;
    }
}

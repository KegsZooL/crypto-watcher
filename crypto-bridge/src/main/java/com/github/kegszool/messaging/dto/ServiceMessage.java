package com.github.kegszool.messaging.dto;

public class ServiceMessage {

    public String data;
    public String chatId;

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getChatId() {
        return chatId;
    }

    public void setChatId(String chatId) {
        this.chatId = chatId;
    }
}

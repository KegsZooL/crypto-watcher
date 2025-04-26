package com.github.kegszool.coin.addition.util;

import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Value;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.message.Message;

import java.util.List;

@Component
public class CoinAdditionMessageBuilder {

    @Value("${menu.coin_addition.messages.success}")
    private String successMsg;

    @Value("${menu.coin_addition.messages.error}")
    private String errorMsg;

    public SendMessage buildSuccessMessage(Message msg, List<String> coins) {
        return SendMessage.builder()
                .chatId(msg.getChatId())
                .text(successMsg + String.join(", ", coins))
                .build();
    }

    public SendMessage buildErrorMessage(Message msg) {
        return SendMessage.builder()
                .chatId(msg.getChatId())
                .text(errorMsg)
                .build();
    }
}


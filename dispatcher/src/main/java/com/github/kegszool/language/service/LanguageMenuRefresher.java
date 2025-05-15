package com.github.kegszool.language.service;

import org.springframework.stereotype.Component;
import com.github.kegszool.menu.ReplyKeyboardService;
import com.github.kegszool.messaging.util.MessageUtils;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

@Component
public class LanguageMenuRefresher {

    private final MessageUtils messageUtils;
    private final ReplyKeyboardService keyboardService;

    public LanguageMenuRefresher(
            MessageUtils messageUtils,
            ReplyKeyboardService keyboardService
    ) {
        this.messageUtils = messageUtils;
        this.keyboardService = keyboardService;
    }

    public SendMessage refreshAndAnswerMessage(CallbackQuery callbackQuery, String selectedLanguage) {

        String chatId = messageUtils.extractChatId(callbackQuery);
        String text = switch (selectedLanguage) {
            case "en" -> "The language has been successfully changed!";
            default -> "Язык был успешно изменён!";
        };

        SendMessage msg = SendMessage.builder()
                .chatId(chatId)
                .text(text)
                .build();

        keyboardService.attachKeyboardByLocale(msg, selectedLanguage);
        return msg;
    }
}
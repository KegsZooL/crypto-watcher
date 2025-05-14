package com.github.kegszool.language.service;

import org.springframework.stereotype.Component;
import com.github.kegszool.messaging.util.MessageUtils;
import com.github.kegszool.menu.util.ReplyKeyboardService;

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

    public SendMessage refreshAndGetRefreshedMenu(CallbackQuery callbackQuery, String selectedLanguage) {

        String chatId = messageUtils.extractChatId(callbackQuery);
        String text = switch (selectedLanguage) {
            case "en" -> "Язык был успешно изменён!";
            default -> "The language has been successfully changed!";
        };

        SendMessage msg = SendMessage.builder()
                .chatId(chatId)
                .text(text)
                .build();

        keyboardService.attachKeyboard(msg, chatId, selectedLanguage);
        return msg;
    }
}
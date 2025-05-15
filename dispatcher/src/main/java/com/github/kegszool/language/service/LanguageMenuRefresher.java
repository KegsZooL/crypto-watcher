package com.github.kegszool.language.service;

import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.annotation.Autowired;

import com.github.kegszool.menu.ReplyKeyboardService;
import com.github.kegszool.messaging.util.MessageUtils;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

@Component
public class LanguageMenuRefresher {

    private final String msgRu;
    private final String msgEn;

    private final MessageUtils messageUtils;
    private final ReplyKeyboardService keyboardService;

    @Autowired
    public LanguageMenuRefresher(
            @Value("${menu.language_change.answer_message.ru}") String msgRu,
            @Value("${menu.language_change.answer_message.en}") String msgEn,
            MessageUtils messageUtils,
            ReplyKeyboardService keyboardService
    ) {
        this.msgRu = msgRu;
        this.msgEn = msgEn;
        this.messageUtils = messageUtils;
        this.keyboardService = keyboardService;
    }

    public SendMessage refreshAndGetAnswerMsg(CallbackQuery callbackQuery, String selectedLanguage) {

        String chatId = messageUtils.extractChatId(callbackQuery);
        String text = switch (selectedLanguage) {
            case "en" -> msgEn;
            default -> msgRu;
        };

        SendMessage msg = SendMessage.builder()
                .chatId(chatId)
                .text(text)
                .build();

        keyboardService.attachKeyboardByLocale(msg, selectedLanguage);
        return msg;
    }
}
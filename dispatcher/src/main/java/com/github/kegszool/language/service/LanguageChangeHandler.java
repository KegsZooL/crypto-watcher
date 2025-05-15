package com.github.kegszool.language.service;

import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;

import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import com.github.kegszool.language.messaging.ChangeLanguageRequestSender;

@Component
public class LanguageChangeHandler {

    private final ChangeLanguageRequestSender requestSender;
    private final LanguageMenuRefresher refresher;

    @Autowired
    public LanguageChangeHandler(ChangeLanguageRequestSender requestSender, LanguageMenuRefresher refresher) {
        this.requestSender = requestSender;
        this.refresher = refresher;
    }

    public SendMessage handle(CallbackQuery callbackQuery, String selectedLanguage) {
        requestSender.send(callbackQuery, selectedLanguage);
        return refresher.refreshAndAnswerMessage(callbackQuery, selectedLanguage);
    }
}
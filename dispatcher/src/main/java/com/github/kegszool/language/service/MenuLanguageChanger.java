package com.github.kegszool.language.service;

import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;

import com.github.kegszool.language.util.LanguageExtractor;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;

@Component
public class MenuLanguageChanger {

    private final LanguageExtractor languageExtractor;
    private final LanguageChangeHandler changeHandler;

    @Autowired
    public MenuLanguageChanger(LanguageExtractor languageExtractor, LanguageChangeHandler changeHandler) {
        this.languageExtractor = languageExtractor;
        this.changeHandler = changeHandler;
    }

    public EditMessageText change(CallbackQuery callbackQuery) {
        String selectedLanguage = languageExtractor.extract(callbackQuery);
        return changeHandler.handle(callbackQuery, selectedLanguage);
    }
}
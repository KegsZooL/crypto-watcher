package com.github.kegszool.language.util;

import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Value;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

@Component
public class LanguageExtractor {

    private final String languagePrefix;

    public LanguageExtractor(@Value("${menu.language_change.prefix}") String languagePrefix) {
        this.languagePrefix = languagePrefix;
    }

    public String extract(CallbackQuery callbackQuery) {
        String data = callbackQuery.getData();
        return data.substring(languagePrefix.length());
    }
}
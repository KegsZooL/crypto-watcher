package com.github.kegszool.localization;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class LocalizationService {

    private final Map<String, String> chatLocales = new ConcurrentHashMap<>();
    private final LocalizationBuffer localizationBuffer;

    @Autowired
    public LocalizationService(LocalizationBuffer localizationBuffer) {
        this.localizationBuffer = localizationBuffer;
    }

    public void setLocale(String chatId, String locale) {
        chatLocales.put(chatId, locale);
    }

    public String getLocale(String chatId) {
        return chatLocales.getOrDefault(chatId, "ru");
    }

    public String getTitleText(String menuName, String chatId) {
        String currentLocale = getLocale(chatId);
        return localizationBuffer.get(menuName).getTitles().get(currentLocale);
    }

    public String getTitleTextByLocale(String menuName, String locale) {
        return localizationBuffer.get(menuName).getTitles().get(locale);
    }

    public String getSectionsConfig(String menuName, String chatId) {
        String currentLocale = getLocale(chatId);
        return localizationBuffer.get(menuName).getSectionsConfig().get(currentLocale);
    }

    public String getSectionsConfigByLocal(String menuName, String locale) {
        return localizationBuffer.get(menuName).getSectionsConfig().get(locale);
    }

    public String getAnswerMessage(String menuName, String chatId) {
        String currentLocale = getLocale(chatId);
        return localizationBuffer.get(menuName).getAnswerMessages()
                .get("default").get(currentLocale);
    }

    public String getAnswerMessage(String menuName, String messageType, String chatId) {
        String currentLocale = getLocale(chatId);
        return localizationBuffer.get(menuName).getAnswerMessages()
                .get(messageType).get(currentLocale);
    }
}
package com.github.kegszool.localization;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

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
        return localizationBuffer.getMenu(menuName).getTitles().get(currentLocale);
    }

    public String getTitleTextByLocale(String menuName, String locale) {
        return localizationBuffer.getMenu(menuName).getTitles().get(locale);
    }

    public String getSectionsConfig(String menuName, String chatId) {
        String currentLocale = getLocale(chatId);
        return localizationBuffer.getMenu(menuName).getSectionsConfig().get(currentLocale);
    }

    public String getSectionsConfigByLocal(String menuName, String locale) {
        return localizationBuffer.getMenu(menuName).getSectionsConfig().get(locale);
    }

    public String getAnswerMessage(String menuName, String chatId) {
        String currentLocale = getLocale(chatId);
        return localizationBuffer.getMenu(menuName).getAnswerMessages()
                .get("default").get(currentLocale);
    }

    public String getAnswerMessage(String menuName, String messageType, String chatId) {
        String currentLocale = getLocale(chatId);
        return localizationBuffer.getMenu(menuName).getAnswerMessages()
                .get(messageType).get(currentLocale);
    }

    public String getCommandDescription(String command, String locale) {
        return localizationBuffer.getDescriptionForCommand(command)
                .getLocaleToDescription().get(locale);
    }
}
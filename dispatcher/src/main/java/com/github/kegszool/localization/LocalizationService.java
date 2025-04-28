package com.github.kegszool.localization;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

@Service
public class LocalizationService {

    @Getter
    @Setter
    private String currentLocale = "ru";

    private final LocalizationBuffer localizationBuffer;

    @Autowired
    public LocalizationService(LocalizationBuffer localizationBuffer) {
        this.localizationBuffer = localizationBuffer;
    }

    public String getTitleText(String menuName) {
        return localizationBuffer.get(menuName).getTitles().get(currentLocale);
    }

    public String getTitleText(String menuName, String locale) {
        return localizationBuffer.get(menuName).getTitles().get(locale);
    }

    public String getSectionsConfig(String menuName) {
        return localizationBuffer.get(menuName).getSectionsConfig().get(currentLocale);
    }

    public String getSectionsConfig(String menuName, String locale) {
        return localizationBuffer.get(menuName).getSectionsConfig().get(locale);
    }

    public String getAnswerMessage(String menuName) {
        return localizationBuffer.get(menuName).getAnswerMessages()
                .get("default").get(currentLocale);
    }

    public String getAnswerMessage(String menuName, String messageType) {
        return localizationBuffer.get(menuName).getAnswerMessages()
                .get(messageType).get(currentLocale);
    }
}
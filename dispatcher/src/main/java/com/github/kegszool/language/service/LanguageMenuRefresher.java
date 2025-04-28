package com.github.kegszool.language.service;

import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Value;

import com.github.kegszool.messaging.util.MessageUtils;
import com.github.kegszool.menu.service.MenuUpdaterService;
import com.github.kegszool.localization.LocalizationService;

import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;

@Component
public class LanguageMenuRefresher {

    private final String menuName;
    private final LocalizationService localizationService;
    private final MenuUpdaterService menuUpdaterService;
    private final MessageUtils messageUtils;

    public LanguageMenuRefresher(
            @Value("${menu.language_change.name}") String menuName,
            LocalizationService localizationService,
            MenuUpdaterService menuUpdaterService,
            MessageUtils messageUtils
    ) {
        this.menuName = menuName;
        this.localizationService = localizationService;
        this.menuUpdaterService = menuUpdaterService;
        this.messageUtils = messageUtils;
    }

    public EditMessageText refreshAndGetRefreshedMenu(CallbackQuery callbackQuery, String selectedLanguage) {
        String localizedConfig = localizationService.getSectionsConfig(menuName, selectedLanguage);
        menuUpdaterService.changeKeyboard(localizedConfig, menuName);
        return messageUtils.createEditMessageByMenuNameWithLocale(callbackQuery, menuName, selectedLanguage);
    }
}
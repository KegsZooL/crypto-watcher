package com.github.kegszool.language;

import com.github.kegszool.localization.LocalizationService;
import com.github.kegszool.menu.service.MenuUpdaterService;
import com.github.kegszool.messaging.util.MessageUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

@Component
public class MenuLanguageChanger {

    private final String languagePrefix;
    private final String menuName;
    private final ChangeLanguageRequestSender requestSender;
    private final LocalizationService localizationService;
    private final MessageUtils messageUtils;
    private final MenuUpdaterService menuUpdaterService;

    public MenuLanguageChanger(
            @Value("${menu.language_change.prefix}") String languagePrefix,
            @Value("${menu.language_change.name}") String menuName,
            ChangeLanguageRequestSender requestSender,
            LocalizationService localizationService,
            MessageUtils messageUtils,
            MenuUpdaterService menuUpdaterService
    ) {
        this.languagePrefix = languagePrefix;
        this.menuName = menuName;
        this.requestSender = requestSender;
        this.localizationService = localizationService;
        this.messageUtils = messageUtils;
        this.menuUpdaterService = menuUpdaterService;
    }

    public EditMessageText change(CallbackQuery callbackQuery) {
        String callbackData = callbackQuery.getData();
        String selectedLanguage = callbackData.substring(languagePrefix.length());
        requestSender.send(callbackQuery, selectedLanguage);
        return createRebuildingLanguageMenuMessage(callbackQuery, selectedLanguage);
    }

    public EditMessageText createRebuildingLanguageMenuMessage(CallbackQuery callbackQuery, String selectedLanguage) {
        String localizedConfig = localizationService.getSectionsConfig(menuName, selectedLanguage);
        menuUpdaterService.changeKeyboard(localizedConfig, menuName);
        return messageUtils.createEditMessageByMenuNameWithLocale(callbackQuery, menuName, selectedLanguage);
    }
}
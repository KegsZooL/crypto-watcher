package com.github.kegszool.menu.base;

import java.util.List;
import java.util.LinkedHashMap;
import jakarta.annotation.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;

import com.github.kegszool.menu.MenuStateStorage;
import com.github.kegszool.menu.util.TitleBuilder;
import com.github.kegszool.menu.util.SectionBuilder;
import com.github.kegszool.menu.util.KeyboardFactory;
import com.github.kegszool.menu.service.MenuSectionService;
import com.github.kegszool.user.messaging.dto.UserData;

import com.github.kegszool.localization.LocalizationService;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

@Component
public abstract class BaseMenu implements Menu {

    @Autowired private KeyboardFactory keyboardFactory;
    @Autowired private MenuSectionService sectionService;
    @Autowired private LocalizationService localizationService;
    @Autowired protected MenuStateStorage menuStateStorage;

    private final SectionBuilder sectionBuilder;
    private final TitleBuilder titleBuilder;

    public BaseMenu(
            @Nullable SectionBuilder sectionBuilder,
            @Nullable TitleBuilder titleBuilder
    ) {
        this.sectionBuilder = sectionBuilder;
        this.titleBuilder = titleBuilder;
    }

    public void initializeMenuForChat(String chatId) {
        String config = getSectionsConfig();
        LinkedHashMap<String, String> sections = sectionService.createSections(config);
        InlineKeyboardMarkup keyboard = keyboardFactory.create(sections, getMaxButtonsPerRow(), getFullWidthSections());

        menuStateStorage.saveSections(getName(), chatId, sections);
        menuStateStorage.saveKeyboard(getName(), chatId, keyboard);
    }

    public void updateMenu(UserData userData, String chatId) {

        String actualLanguage = userData.getUserPreference().interfaceLanguage();
        if (titleBuilder != null) {
            String newTitle = titleBuilder.buildTitle(userData,  actualLanguage);
            menuStateStorage.saveTitle(getName(), chatId, newTitle);
        }

        String newConfig = "";
        if (sectionBuilder != null) {
            newConfig = sectionBuilder.buildSectionsConfig(userData, actualLanguage);
        }

        LinkedHashMap<String, String> sections = menuStateStorage.getSections(getName(), chatId);
        if (sections == null) {
            initializeMenuForChat(chatId);
            return;
        }
        sectionService.update(sections, newConfig, true, getName(), actualLanguage);
        InlineKeyboardMarkup newKeyboard = keyboardFactory.create(sections, getMaxButtonsPerRow(), getFullWidthSections());
        menuStateStorage.saveKeyboard(getName(), chatId, newKeyboard);
    }

    public abstract boolean hasDataChanged(UserData userData, String chatId);

    public boolean isLocaleChanged(UserData userData, String chatId) {
        String currentLocale = localizationService.getLocale(chatId);
        return !userData.getUserPreference().interfaceLanguage().equals(currentLocale);
    }

    protected abstract String getSectionsConfig();
    protected abstract int getMaxButtonsPerRow();
    protected abstract List<String> getFullWidthSections();

    @Override
    public InlineKeyboardMarkup getKeyboardMarkup(String chatId) {
        return menuStateStorage.getKeyboard(getName(), chatId);
    }

    public void changeMenuKeyboard(String sectionsConfig, String chatId) {
        LinkedHashMap<String, String> sections = sectionService.createSections(sectionsConfig);
        menuStateStorage.saveSections(getName(), chatId, sectionService.createSections(sectionsConfig));

        InlineKeyboardMarkup keyboard = keyboardFactory.create(sections, getMaxButtonsPerRow(), getFullWidthSections());
        menuStateStorage.saveKeyboard(getName(), chatId, keyboard);
    }

    public boolean hasTitleBuilder() {
        return titleBuilder != null;
    }
}
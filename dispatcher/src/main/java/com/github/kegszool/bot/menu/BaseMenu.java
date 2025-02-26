package com.github.kegszool.bot.menu;

import com.github.kegszool.utils.KeyboardFactory;
import com.github.kegszool.exception.bot.menu.MenuException;
import com.github.kegszool.bot.menu.service.section.MenuSectionService;

import org.springframework.beans.factory.annotation.Autowired;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

import java.util.List;
import java.util.LinkedHashMap;

import lombok.extern.log4j.Log4j2;
import jakarta.annotation.PostConstruct;

@Log4j2
public abstract class BaseMenu implements Menu {

    @Autowired private KeyboardFactory keyboardFactory;
    @Autowired private MenuSectionService sectionService;

    protected LinkedHashMap<String, String> SECTIONS = new LinkedHashMap<>();
    protected InlineKeyboardMarkup menuKeyboard;

    @PostConstruct
    protected void initializeMenu() throws MenuException {
        String sectionsConfig = getSectionsConfig();
        SECTIONS = sectionService.createSections(sectionsConfig);
        menuKeyboard = keyboardFactory.create(SECTIONS, getMaxButtonsPerRow(), getFullWidthSections());
    }

    public void updateSections(String sectionsConfig, boolean saveActionButton) {
        sectionService.updateSections(SECTIONS, sectionsConfig, saveActionButton);
        menuKeyboard = keyboardFactory.create(SECTIONS, getMaxButtonsPerRow(), getFullWidthSections());
    }

    protected abstract String getSectionsConfig();
    protected abstract int getMaxButtonsPerRow();
    protected abstract List<String> getFullWidthSections();

    @Override
    public InlineKeyboardMarkup getKeyboardMarkup() {
        return menuKeyboard;
    }
}
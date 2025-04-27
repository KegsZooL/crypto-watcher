package com.github.kegszool.menu.base;

import com.github.kegszool.menu.util.SectionBuilder;
import jakarta.annotation.Nullable;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;

import com.github.kegszool.user.dto.UserData;
import com.github.kegszool.menu.service.MenuUpdaterService;

import java.util.List;
import java.util.LinkedHashMap;
import com.github.kegszool.menu.util.KeyboardFactory;
import com.github.kegszool.menu.service.MenuSectionService;
import com.github.kegszool.menu.exception.base.MenuException;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

@Component
public abstract class BaseMenu implements Menu {

    @Autowired private KeyboardFactory keyboardFactory;
    @Autowired private MenuSectionService sectionService;
    @Autowired private MenuUpdaterService menuUpdaterService;

    @Nullable
    private final SectionBuilder sectionBuilder;

    public BaseMenu(@Nullable SectionBuilder sectionBuilder) {
        this.sectionBuilder = sectionBuilder;
    }

    protected LinkedHashMap<String, String> SECTIONS = new LinkedHashMap<>();
    protected InlineKeyboardMarkup menuKeyboard;

    @PostConstruct
    protected void initializeMenu() throws MenuException {
        String sectionsConfig = getSectionsConfig();
        SECTIONS = sectionService.createSections(sectionsConfig);
        menuKeyboard = keyboardFactory.create(SECTIONS, getMaxButtonsPerRow(), getFullWidthSections());

        menuUpdaterService.registerMenu(getName(), this);
    }

    public void updateMenu(UserData userData) {
        String newConfig = "";
        if (sectionBuilder != null) {
            newConfig = sectionBuilder.buildSectionsConfig(userData);
        }
        sectionService.update(SECTIONS, newConfig, true, getName());
        menuKeyboard = keyboardFactory.create(SECTIONS, getMaxButtonsPerRow(), getFullWidthSections());
    }

    public abstract boolean hasDataChanged(UserData userData);

    protected abstract String getSectionsConfig();
    protected abstract int getMaxButtonsPerRow();
    protected abstract List<String> getFullWidthSections();

    @Override
    public InlineKeyboardMarkup getKeyboardMarkup() {
        return menuKeyboard;
    }
}
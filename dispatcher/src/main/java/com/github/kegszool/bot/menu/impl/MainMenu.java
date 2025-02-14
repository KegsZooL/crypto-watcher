package com.github.kegszool.bot.menu.impl;

import com.github.kegszool.bot.menu.BaseMenu;

import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Value;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

@Component
public class MainMenu extends BaseMenu {

    @Value("${menu.main.name}")
    private String NAME;

    @Value("${menu.main.title}")
    private String TITLE;

    @Value("${menu.main.sections}")
    private String MENU_SECTIONS_CONFIG;

    @Value("${menu.main.max_buttons_per_row}")
    private int MAX_BUTTONS_PER_ROW;

    @Override
    protected String getSectionsConfig() {
        return MENU_SECTIONS_CONFIG;
    }

    @Override
    protected int getMaxButtonsPerRow() {
        return MAX_BUTTONS_PER_ROW;
    }

    @Override
    public InlineKeyboardMarkup getKeyboard() {
        return menuKeyboard;
    }

    @Override
    public String getTitle() {
        return TITLE;
    }

    @Override
    public String getName() {
        return NAME;
    }
}
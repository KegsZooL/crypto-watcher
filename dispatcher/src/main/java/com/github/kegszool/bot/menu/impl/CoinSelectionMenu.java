package com.github.kegszool.bot.menu.impl;

import com.github.kegszool.bot.menu.BaseMenu;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

@Component
public class CoinSelectionMenu extends BaseMenu {

    @Value("${menu.coin_selection.name}")
    private String NAME;

    @Value("${menu.coin_selection.title}")
    private String TITLE;

    @Value("${menu.coin_selection.sections}")
    private String MENU_SECTIONS_CONFIG;

    @Override
    protected String getSectionsConfig() {
        return MENU_SECTIONS_CONFIG;
    }

    @Override
    public InlineKeyboardMarkup get() {
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
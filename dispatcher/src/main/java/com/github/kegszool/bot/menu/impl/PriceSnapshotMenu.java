package com.github.kegszool.bot.menu.impl;

import com.github.kegszool.bot.menu.BaseMenu;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

@Component
public class PriceSnapshotMenu extends BaseMenu {

    @Value("${menu.name[5].price_snapshot}")
    private String NAME;

    @Value("${menu.title[2].coin_price_snapshot}")
    private String TITLE;

    @Value("${menu.section[2].coin_price_snapshot}")
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
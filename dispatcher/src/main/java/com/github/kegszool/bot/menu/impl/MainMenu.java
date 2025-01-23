package com.github.kegszool.bot.menu.impl;

import com.github.kegszool.bot.menu.BaseMenu;
import com.github.kegszool.bot.menu.Menu;
import com.github.kegszool.utils.KeyboardFactory;
import jakarta.annotation.PostConstruct;
import lombok.extern.java.Log;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

import java.util.LinkedHashMap;
import java.util.Map;

@Component
public class MainMenu extends BaseMenu {

    @Value("${menu.main.name}")
    private String NAME;

    @Value("${menu.main.title}")
    private String TITLE;

    @Value("${menu.main.sections}")
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
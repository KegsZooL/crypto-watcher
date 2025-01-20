package com.github.kegszool.bot.menu.impl;

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
@Log4j2
public class MainMenu implements Menu {

    //TODO: add logging

    @Value("${menu.pages[4].main}")
    private String NAME;

    @Value("${menu.titles[1].main}")
    private String TITLE;

    @Value("${menu.sections[0].main}")
    private String MENU_SECTIONS_CONFIG;

    private static final Map<String, String> SECTIONS = new LinkedHashMap<>();

    private InlineKeyboardMarkup menuKeyboard;

    @PostConstruct
    private void initializeMenu() {
        if(MENU_SECTIONS_CONFIG != null) {
            String[] pairs = MENU_SECTIONS_CONFIG.split(",");
            for (String pair : pairs) {
                String[] keyValue = pair.split(":");
                if(keyValue.length == 2) {
                    SECTIONS.put(keyValue[0], keyValue[1]);
                }
            }
            menuKeyboard = KeyboardFactory.create(SECTIONS);
        }
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
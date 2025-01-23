package com.github.kegszool.bot.menu;

import com.github.kegszool.utils.KeyboardFactory;
import jakarta.annotation.PostConstruct;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

import java.util.LinkedHashMap;
import java.util.Map;

public abstract class BaseMenu implements Menu {

    protected final Map<String, String> SECTIONS = new LinkedHashMap<>();
    protected InlineKeyboardMarkup menuKeyboard;

    @PostConstruct
    protected void initializeMenu() {
        String SECTIONS_CONFIG = getSectionsConfig();
        if(SECTIONS_CONFIG != null && !SECTIONS_CONFIG.isEmpty()) {
            parseSectionConfig(SECTIONS_CONFIG);
            menuKeyboard = KeyboardFactory.create(SECTIONS);
        } else {  } //TODO: ДОПИСАТЬ
    }

    protected abstract String getSectionsConfig();

    private void parseSectionConfig(String menuSectionsConfig) {
        String[] pairs = menuSectionsConfig.split(",");
        for (String pair : pairs) {
            String[] keyValue = pair.split(":");
            if(keyValue.length == 2) {
                SECTIONS.put(keyValue[0], keyValue[1]);
            } else { } //TODO: дописать
        }
    }
}
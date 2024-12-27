package com.github.kegszool.menu.impl;

import com.github.kegszool.menu.Menu;
import com.github.kegszool.utils.KeyboardFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

import java.util.Map;

@Component
public class MainMenu implements Menu {

    @Value("${menu.pages[4].main}")
    private String PAGE_NAME;

    private static final Map<String, String> SECTIONS = Map.of(
            "exchange_rate_of_coins", "Курс монет",
            "alerts", "Оповещения",
            "settings", "Настройки"
    );

    private static final String TITLE = "Привет! Я бот, который поможет тебе уследить за изменениями курсов монет.\n\n" +
            "Для более удобного взаимодействия со мной можешь использовать меню";

    private final InlineKeyboardMarkup inlineKeyboardMarkup;

    public MainMenu() {
        inlineKeyboardMarkup = KeyboardFactory.create(SECTIONS);
    }

    @Override
    public InlineKeyboardMarkup get() {
        return inlineKeyboardMarkup;
    }

    @Override
    public String getTitle() {
        return TITLE;
    }

    @Override
    public String getPageName() {
        return PAGE_NAME;
    }
}

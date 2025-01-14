package com.github.kegszool.bot.menu.impl;

import com.github.kegszool.bot.menu.Menu;
import com.github.kegszool.utils.KeyboardFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

import java.util.Map;

@Component
public class CoinExchangeRateMenu implements Menu {

    @Value("${menu.pages[0].exchange_rate}")
    private String PAGE_NAME;

    private static final Map<String, String> SECTIONS = Map.of(
            "coin_BTC-USD-SWAP", "BTC",
            "coin_ETH", "ETH",
            "coin_DOGE", "DOGE",
            "back", "Назад"
    );

    private static final String TITLE = "Выберите монету!";

    private final InlineKeyboardMarkup inlineKeyboardMarkup;

    public CoinExchangeRateMenu() {
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
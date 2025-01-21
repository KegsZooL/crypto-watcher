package com.github.kegszool.bot.menu.impl;

import com.github.kegszool.bot.menu.Menu;
import com.github.kegszool.utils.KeyboardFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

import java.util.LinkedHashMap;
import java.util.Map;

@Component
public class CoinExchangeRateMenu implements Menu {

    //TODO: implement a data base (read the user's favorite coins)

    @Value("${menu.pages[0].exchange_rate}")
    private String NAME;

    @Value("${menu.titles[0].exchange_rate}")
    private String TITLE;

    private static final Map<String, String> SECTIONS = new LinkedHashMap<>();

    static {
        SECTIONS.put("coin_BTC-USD-SWAP", "BTC");
        SECTIONS.put("coin_ETH-USD-SWAP", "ETH");
        SECTIONS.put("coin_DOGE-USD-SWAP", "DOGE");
        SECTIONS.put("back", "Back");
    }


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
    public String getName() {
        return NAME;
    }
}
package com.github.kegszool.UI.menu.impl;

import com.github.kegszool.UI.menu.Menu;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

@Component
public class CoinExchangeRateMenu implements Menu {

    private static String TITLE = "Меню курсов";


    @Override
    public InlineKeyboardMarkup getMenu() {
        return null;
    }

    public String getTitle (){
        return TITLE;
    }

}
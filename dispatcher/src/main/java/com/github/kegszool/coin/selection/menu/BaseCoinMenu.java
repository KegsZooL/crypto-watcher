package com.github.kegszool.coin.selection.menu;

import java.util.LinkedHashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;

import com.github.kegszool.menu.base.BaseMenu;
import com.github.kegszool.menu.util.SectionBuilder;

import com.github.kegszool.coin.dto.CoinDto;
import com.github.kegszool.coin.dto.FavoriteCoinDto;

import com.github.kegszool.user.messaging.dto.UserDto;
import com.github.kegszool.user.messaging.dto.UserData;

public abstract class BaseCoinMenu extends BaseMenu {

    @Value("${menu.coin_selection.prefix.currency}")
    private String CURRENCY_PREFIX;

    public BaseCoinMenu(SectionBuilder sectionBuilder) {
        super(sectionBuilder);
    }

    public List<FavoriteCoinDto> getAllFavoriteCoins(UserDto user, String chatId) {
        LinkedHashMap<String, String> sections = menuStateStorage.getSections(getName(), chatId);
        if (sections == null) {
            return List.of();
        }

        return sections.keySet().stream()
                .filter(key -> key.endsWith(CURRENCY_PREFIX))
                .map(key -> new FavoriteCoinDto(user, new CoinDto(sections.get(key))))
                .toList();
    }

    @Override
    public boolean hasDataChanged(UserData userData, String chatId) {
        List<FavoriteCoinDto> allCoins = getAllFavoriteCoins(userData.getUser(), chatId);
        return !allCoins.equals(userData.getFavoriteCoins()) || isLocaleChanged(userData, chatId);
    }
}
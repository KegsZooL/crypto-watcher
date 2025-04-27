package com.github.kegszool.coin;

import java.util.List;
import org.springframework.beans.factory.annotation.Value;

import com.github.kegszool.menu.base.BaseMenu;
import com.github.kegszool.menu.util.SectionBuilder;

import com.github.kegszool.coin.dto.CoinDto;
import com.github.kegszool.coin.dto.FavoriteCoinDto;

import com.github.kegszool.user.dto.UserDto;
import com.github.kegszool.user.dto.UserData;

public abstract class FavoriteCoinMenu extends BaseMenu {

    @Value("${menu.coin_selection.prefix.currency}")
    private String CURRENCY_PREFIX;

    public FavoriteCoinMenu(SectionBuilder sectionBuilder) {
        super(sectionBuilder);
    }

    public List<FavoriteCoinDto> getAllFavoriteCoins(UserDto user) {
        return SECTIONS.keySet().stream()
                .filter(k -> k.endsWith(CURRENCY_PREFIX))
                .map(k -> new FavoriteCoinDto(user, new CoinDto(SECTIONS.get(k))))
                .toList();
    }

    @Override
    public boolean hasDataChanged(UserData userData) {
        List<FavoriteCoinDto> allCoins = getAllFavoriteCoins(userData.getUser());
        return !allCoins.equals(userData.getFavoriteCoins());
    }
}
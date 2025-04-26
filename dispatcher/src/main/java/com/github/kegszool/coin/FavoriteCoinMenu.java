package com.github.kegszool.coin;


import com.github.kegszool.menu.util.SectionBuilder;
import com.github.kegszool.menu.service.MenuUpdaterService;
import com.github.kegszool.coin.dto.CoinDto;
import com.github.kegszool.coin.dto.FavoriteCoinDto;
import com.github.kegszool.user.dto.UserData;
import com.github.kegszool.user.dto.UserDto;
import com.github.kegszool.user.menu.UserDataDependentBaseMenu;

import java.util.List;
import org.springframework.beans.factory.annotation.Value;

public abstract class FavoriteCoinMenu extends UserDataDependentBaseMenu {

    @Value("${menu.coin_selection.prefix[1].currency}")
    private String CURRENCY_PREFIX;

    protected FavoriteCoinMenu(
            MenuUpdaterService menuUpdaterService,
            SectionBuilder sectionBuilder
    ) {
        super(menuUpdaterService, sectionBuilder);
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
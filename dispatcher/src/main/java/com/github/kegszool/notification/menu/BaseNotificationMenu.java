package com.github.kegszool.notification.menu;

import com.github.kegszool.coin.dto.CoinDto;
import com.github.kegszool.coin.dto.FavoriteCoinDto;

import com.github.kegszool.menu.base.BaseMenu;
import com.github.kegszool.menu.util.SectionBuilder;

import com.github.kegszool.user.messaging.dto.UserData;
import com.github.kegszool.notification.messaging.dto.NotificationDto;

import java.util.LinkedHashMap;
import java.util.Set;
import java.util.List;
import java.util.stream.Stream;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Value;

public abstract class BaseNotificationMenu extends BaseMenu {

    @Value("${menu.coin_selection.prefix.currency}")
    private String CURRENCY_PREFIX;

    public BaseNotificationMenu(SectionBuilder sectionBuilder) {
        super(sectionBuilder);
    }

    private boolean isCoinSetEqualToSections(List<CoinDto> coinsFromUserData, String chatId) {
        LinkedHashMap<String, String> sections = menuStateStorage.getSections(getName(), chatId);
        if (sections == null) {
            return coinsFromUserData.isEmpty();
        }

        Set<String> coinsFromSections = sections.keySet().stream()
                .filter(k -> k.endsWith(CURRENCY_PREFIX))
                .map(sections::get)
                .collect(Collectors.toSet());

        Set<String> coinsFromUser = coinsFromUserData.stream()
                .map(CoinDto::getName)
                .collect(Collectors.toSet());

        return coinsFromSections.equals(coinsFromUser);
    }

    @Override
    public boolean hasDataChanged(UserData userData, String chatId) {
        List<CoinDto> allCoins = Stream.concat(
                userData.getFavoriteCoins().stream().map(FavoriteCoinDto::getCoin),
                userData.getNotifications().stream().map(NotificationDto::getCoin)
        ).distinct().toList();

        return !isCoinSetEqualToSections(allCoins, chatId) || isLocaleChanged(userData, chatId);
    }
}

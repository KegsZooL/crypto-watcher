package com.github.kegszool.notification.util;

import java.util.Set;
import java.util.List;
import java.util.HashSet;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.github.kegszool.coin.dto.CoinDto;
import com.github.kegszool.coin.dto.FavoriteCoinDto;

import com.github.kegszool.menu.util.SectionBuilder;
import com.github.kegszool.user.messaging.dto.UserData;
import com.github.kegszool.notification.messaging.dto.NotificationDto;

@Component
public class CoinNotificationSectionBuilder implements SectionBuilder {

    private final String notificationPrefix;

    public CoinNotificationSectionBuilder(@Value("${menu.select_coin_notification.prefix}")
                                          String selectedCoinPrefix) {
        this.notificationPrefix = selectedCoinPrefix;
    }

    @Override
    public String buildSectionsConfig(UserData userData) {

        Set<String> addedCoins = new HashSet<>();
        List<String> sections = new ArrayList<>();

        for (FavoriteCoinDto favorite : userData.getFavoriteCoins()) {
            CoinDto coin = favorite.getCoin();
            addedCoins.add(coin.getName());
            String sectionEntry = buildEntry(coin);
            sections.add(sectionEntry);
        }

        for (NotificationDto notification : userData.getNotifications()) {
            CoinDto coin = notification.getCoin();
            if (addedCoins.add(coin.getName())) {
                String sectionEntry = buildEntry(coin);
                sections.add(sectionEntry);
            }
        }
        return String.join(",", sections);
    }

    private String buildEntry(CoinDto coin) {
        String coinName = coin.getName();
        String callbackData = notificationPrefix + coinName;
        return String.format("%s:%s", callbackData, coinName);
    }
}
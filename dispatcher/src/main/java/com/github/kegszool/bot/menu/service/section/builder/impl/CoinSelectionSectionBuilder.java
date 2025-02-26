package com.github.kegszool.bot.menu.service.section.builder.impl;

import com.github.kegszool.bot.menu.service.section.builder.SectionBuilder;
import com.github.kegszool.messaging.dto.database_entity.CoinDto;
import com.github.kegszool.messaging.dto.database_entity.UserData;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class CoinSelectionSectionBuilder implements SectionBuilder {

   	@Value("${menu.coin_selection.prefix[0].coin}")
    private String COIN_PREFIX;

    @Value("${menu.coin_selection.prefix[1].currency}")
    private String CURRENCY_PREFIX;

    @Override
    public String buildSectionsConfig(UserData userData) {
        return userData.getFavoriteCoins().stream()
                .map(favoriteCoin -> {
                    CoinDto coin = favoriteCoin.getCoin();
                    String coinName = coin.getName();
                    String coinWithPrefixes = COIN_PREFIX + coinName + CURRENCY_PREFIX;
                    return String.format("%s:%s", coinWithPrefixes, coinName);
                })
                .collect(Collectors.joining(","));
    }
}
package com.github.kegszool.coin.selection.util;

import java.util.stream.Collectors;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Value;

import com.github.kegszool.coin.dto.CoinDto;
import com.github.kegszool.menu.util.SectionBuilder;
import com.github.kegszool.user.messaging.dto.UserData;

@Component
public class CoinSelectionSectionBuilder implements SectionBuilder {

   	@Value("${menu.coin_selection.prefix.coin}")
    private String COIN_PREFIX;

    @Value("${menu.coin_selection.prefix.currency}")
    private String CURRENCY_PREFIX;

    @Override
    public String buildSectionsConfig(UserData userData, String locale) {
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
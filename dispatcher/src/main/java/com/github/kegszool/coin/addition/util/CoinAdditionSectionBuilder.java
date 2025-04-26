package com.github.kegszool.coin.addition.util;

import com.github.kegszool.coin.dto.CoinDto;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Value;

import java.util.stream.Collectors;
import com.github.kegszool.user.dto.UserData;
import com.github.kegszool.menu.util.SectionBuilder;

@Component
public class CoinAdditionSectionBuilder implements SectionBuilder {

    private final String noOpPrefix;
    private final String currencyPrefix;

    public CoinAdditionSectionBuilder(
            @Value("${menu.action.noop_prefix}") String noOpPrefix,
            @Value("${menu.coin_selection.prefix[1].currency}") String currencyPrefix
    ) {
        this.noOpPrefix = noOpPrefix;
        this.currencyPrefix = currencyPrefix;
    }

    @Override
    public String buildSectionsConfig(UserData userData) {
        return userData.getFavoriteCoins().stream()
                .map(favoriteCoin -> {
                    CoinDto coin = favoriteCoin.getCoin();

                    String coinName = coin.getName();
                    String callbackData = noOpPrefix + coinName + currencyPrefix;

                    return String.format("%s:%s", callbackData, coinName);
                })
                .collect(Collectors.joining(","));
    }
}
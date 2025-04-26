package com.github.kegszool.coin.deletion.util;

import com.github.kegszool.coin.dto.CoinDto;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.annotation.Autowired;

import com.github.kegszool.user.dto.UserData;
import com.github.kegszool.menu.util.SectionBuilder;
import com.github.kegszool.coin.selection.state.MenuSelectionBuffer;

import java.util.stream.Collectors;

@Component
public class CoinDeletionSectionBuilder implements SectionBuilder {

    private final String menuName;
    private final String coinPrefix, currencyPrefix;
    private final String selectedPrefix, unselectedPrefix;

    private final MenuSelectionBuffer selectionBuffer;

    @Autowired
    public CoinDeletionSectionBuilder(
            @Value("${menu.coin_deletion_menu.name}") String menuName,
            @Value("${menu.coin_selection.prefix[0].coin}") String coinPrefix,
            @Value("${menu.coin_selection.prefix[1].currency}") String currencyPrefix,
            @Value("${menu.coin_deletion_menu.prefix.selected_coin_prefix}") String selectedPrefix,
            @Value("${menu.coin_deletion_menu.prefix.unselected_coin_prefix}") String unselectedPrefix,
            MenuSelectionBuffer selectionBuffer
    ) {
        this.selectionBuffer = selectionBuffer;
        this.menuName = menuName;
        this.coinPrefix = coinPrefix;
        this.currencyPrefix = currencyPrefix;
        this.selectedPrefix = selectedPrefix;
        this.unselectedPrefix = unselectedPrefix;
    }

    @Override
    public String buildSectionsConfig(UserData userData) {
        var selectedButtons = selectionBuffer.getSelected(menuName);
        return userData.getFavoriteCoins().stream()
                .map(favoriteCoin -> {
                    CoinDto coin = favoriteCoin.getCoin();
                    String coinName = coin.getName();

                    boolean isSelected = selectedButtons.stream()
                            .anyMatch(button -> button.getCallbackData().contains(coinName));

                    String basePrefix = coinPrefix + coinName + currencyPrefix;
                    String prefixOfSelectionState = isSelected ? selectedPrefix : unselectedPrefix;
                    String prefix = prefixOfSelectionState + basePrefix;
                    return String.format("%s:%s", prefix, coinName);
                })
                .collect(Collectors.joining(","));
    }
}
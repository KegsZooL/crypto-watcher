package com.github.kegszool.bot.menu.service.section.builder.impl;

import com.github.kegszool.bot.menu.service.section.builder.SectionBuilder;
import com.github.kegszool.bot.menu.service.selection.SelectionStateRepository;
import com.github.kegszool.messaging.dto.database_entity.CoinDto;
import com.github.kegszool.messaging.dto.database_entity.UserData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;


@Component
public class CoinDeletionSectionBuilder implements SectionBuilder {

    @Value("${menu.coin_selection.prefix[0].coin}")
    private String COIN_PREFIX;

    @Value("${menu.coin_selection.prefix[1].currency}")
    private String CURRENCY_PREFIX;

    @Value("${menu.coin_deletion_menu.name}")
    private String COIN_DELETION_MENU_NAME;

    @Value("${menu.coin_deletion_menu.prefix.selected_coin_prefix}")
    private String SELECTED_DELETION_PREFIX;

    @Value("${menu.coin_deletion_menu.prefix.unselected_coin_prefix}")
    private String UNSELECTED_DELETION_PREFIX;

    private final SelectionStateRepository selectionStateRepository;

    @Autowired
    public CoinDeletionSectionBuilder(SelectionStateRepository selectionStateRepository) {
        this.selectionStateRepository = selectionStateRepository;
    }

    @Override
    public String buildSectionsConfig(UserData userData) {
        var selectedButtons = selectionStateRepository.getSelected(COIN_DELETION_MENU_NAME);
        return userData.getFavoriteCoins().stream()
                .map(favoriteCoin -> {
                    CoinDto coin = favoriteCoin.getCoin();
                    String coinName = coin.getName();

                    boolean isSelected = selectedButtons.stream()
                            .anyMatch(button -> button.getCallbackData().contains(coinName));

                    String basePrefix = COIN_PREFIX + coinName + CURRENCY_PREFIX;
                    String prefixOfSelectionState = isSelected ? SELECTED_DELETION_PREFIX : UNSELECTED_DELETION_PREFIX;
                    String prefix = prefixOfSelectionState + basePrefix;
                    return String.format("%s:%s", prefix, coinName);
                })
                .collect(Collectors.joining(","));
    }
}
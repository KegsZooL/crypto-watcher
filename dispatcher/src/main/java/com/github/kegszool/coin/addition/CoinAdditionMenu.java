package com.github.kegszool.coin.addition;

import java.util.List;

import com.github.kegszool.coin.addition.util.CoinAdditionSectionBuilder;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.annotation.Autowired;

import com.github.kegszool.coin.FavoriteCoinMenu;

@Component
public class CoinAdditionMenu extends FavoriteCoinMenu {

    private final String name;
    private final String title;
    private final String config;

    private final int maxButtonsPerRow;
    private final String callbackDataForFullWidthButton;

    @Autowired
    public CoinAdditionMenu(
            @Value("${menu.coin_addition.name}") String name,
            @Value("${menu.coin_addition.title.ru}") String title,
            @Value("${menu.coin_addition.max_buttons_per_row}") int maxButtonsPerRow,
            @Value("${menu.coin_addition.sections.ru}") String config,
            @Value("${menu.action.back}") String callbackDataForFullWidthButton,
            CoinAdditionSectionBuilder sectionBuilder
    ) {
        super(sectionBuilder);
        this.name = name;
        this.title = title;
        this.config = config;
        this.maxButtonsPerRow = maxButtonsPerRow;
        this.callbackDataForFullWidthButton = callbackDataForFullWidthButton;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getTitle() {
        return title;

    }

    @Override
    protected String getSectionsConfig() {
        return config;
    }

    @Override
    protected int getMaxButtonsPerRow() {
        return maxButtonsPerRow;
    }

    @Override
    protected List<String> getFullWidthSections() {
        return List.of(callbackDataForFullWidthButton);
    }
}
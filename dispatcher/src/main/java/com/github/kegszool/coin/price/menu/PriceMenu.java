package com.github.kegszool.coin.price.menu;

import java.util.List;
import java.util.Collections;

import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Value;

import com.github.kegszool.user.messaging.dto.UserData;
import com.github.kegszool.menu.base.BaseMenu;

@Component
public class PriceMenu extends BaseMenu {

    private final String name;
    private final String title;
    private final String config;
    private final int maxButtonsPerRow;

    public PriceMenu(
            @Value("${menu.price_snapshot.name}") String name,
            @Value("${menu.price_snapshot.title.ru}") String title,
            @Value("${menu.price_snapshot.sections.ru}") String config,
            @Value("${menu.price_snapshot.max_buttons_per_row}") int maxButtonsPerRow

    ) {
        super(null);
        this.name = name;
        this.title = title;
        this.config = config;
        this.maxButtonsPerRow = maxButtonsPerRow;
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
        return Collections.emptyList();
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public boolean hasDataChanged(UserData userData, String chatId) {
        return isLocaleChanged(userData, chatId);
    }
}
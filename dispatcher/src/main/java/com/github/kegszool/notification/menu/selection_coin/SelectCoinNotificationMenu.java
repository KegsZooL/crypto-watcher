package com.github.kegszool.notification.menu.selection_coin;

import java.util.List;
import java.util.Collections;

import com.github.kegszool.notification.menu.BaseNotificationMenu;
import com.github.kegszool.notification.util.CoinNotificationSectionBuilder;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Value;

@Component
@Scope("prototype")
public class SelectCoinNotificationMenu extends BaseNotificationMenu {

    private final String name;
    private final String title;
    private final String config;
    private final int maxButtonsPerRow;

    public SelectCoinNotificationMenu(
            @Value("${menu.select_coin_notification.name}") String name,
            @Value("${menu.select_coin_notification.title.ru}") String title,
            @Value("${menu.select_coin_notification.sections.ru}") String config,
            @Value("${menu.select_coin_notification.max_buttons_per_row}") int maxButtonsPerRow,
            CoinNotificationSectionBuilder sectionBuilder
    ) {
        super(sectionBuilder, null);
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
}
package com.github.kegszool.notification.setting;

import com.github.kegszool.menu.base.BaseMenu;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Value;

import java.util.List;
import java.util.Collections;
import com.github.kegszool.user.messaging.dto.UserData;

@Component
public class SetCoinNotificationMenu extends BaseMenu {

    private final String name;
    private final String title;
    private final String config;
    private final int maxButtonsPerRow;

    public SetCoinNotificationMenu(
            @Value("${menu.set_coin_notification.name}") String name,
            @Value("${menu.set_coin_notification.title.ru}") String title,
            @Value("${menu.sections_config.set_coin_notification.ru}") String config,
            @Value("${menu.set_coin_notification.max_buttons_per_row}") int maxButtonsPerRow
    ) {
        super(null, null);
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
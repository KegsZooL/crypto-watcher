package com.github.kegszool.notification.menu.creation;

import java.util.List;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Value;

import com.github.kegszool.user.dto.UserData;
import com.github.kegszool.menu.base.BaseMenu;

@Component
public class NotificationCreationMenu extends BaseMenu {

    private final String name;
    private final String title;
    private final String config;

    private final int maxButtonsPerRow;
    private final String callbackDataForFullWidthButton;

    public NotificationCreationMenu(
            @Value("${menu.notification_creation.name}") String name,
            @Value("${menu.notification_creation.title.ru}") String title,
            @Value("${menu.notification_creation.max_buttons_per_row}") int maxButtonsPerRow,
            @Value("${menu.notification_creation.sections.ru}") String config,
            @Value("${menu.action.back}") String callbackDataForFullWidthButton
    ) {
        super(null);
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

    @Override
    public boolean hasDataChanged(UserData userData) {
        return isLocaleChanged(userData);
    }
}
package com.github.kegszool.settings;

import java.util.List;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Value;

import com.github.kegszool.user.messaging.dto.UserData;
import com.github.kegszool.menu.base.BaseMenu;

@Component
public class SettingsMenu extends BaseMenu {

    private final String name;
    private final String title;
    private final String config;
    private final int maxButtonsPerRow;

    private final String callbackDataForFullWidthButton;

    public SettingsMenu(
            @Value("${menu.settings.name}") String name,
            @Value("${menu.settings.title.ru}") String title,
            @Value("${menu.settings.sections.ru}") String config,
            @Value("${menu.settings.max_buttons_per_row}") int maxButtonsPerRow,
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
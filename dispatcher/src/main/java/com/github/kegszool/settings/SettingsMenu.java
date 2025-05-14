package com.github.kegszool.settings;

import java.util.List;

import com.github.kegszool.menu.CalledMenu;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Value;

import com.github.kegszool.menu.base.BaseMenu;
import com.github.kegszool.user.messaging.dto.UserData;

@Component
public class SettingsMenu extends BaseMenu implements CalledMenu {

    private final String name;
    private final String title;
    private final String config;
    private final int maxButtonsPerRow;

    private final String callbackDataForFullWidthButton;
    private final List<String> namesOfMenuSequence;

    public SettingsMenu(
            @Value("${menu.settings.name}") String name,
            @Value("${menu.settings.title.ru}") String title,
            @Value("${menu.settings.sections.ru}") String config,
            @Value("${menu.settings.max_buttons_per_row}") int maxButtonsPerRow,
            @Value("${menu.action.back}") String callbackDataForFullWidthButton,
            @Value("${menu.settings.sequence}") List<String> namesOfMenuSequence
    ) {
        super(null, null);
        this.name = name;
        this.title = title;
        this.config = config;
        this.maxButtonsPerRow = maxButtonsPerRow;
        this.callbackDataForFullWidthButton = callbackDataForFullWidthButton;
        this.namesOfMenuSequence = namesOfMenuSequence;
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
    public boolean hasDataChanged(UserData userData, String chatId) {
        return isLocaleChanged(userData, chatId);
    }

    @Override
    public List<String> getMenuSequence() {
        return namesOfMenuSequence;
    }
}
package com.github.kegszool.menu.base.main;

import com.github.kegszool.menu.base.BaseMenu;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Value;

import java.util.List;
import java.util.Collections;
import com.github.kegszool.user.dto.UserData;

@Component
public class MainMenu extends BaseMenu {

    private final String name;
    private final String title;
    private final String config;
    private final int maxButtonsPerRow;

    public MainMenu(
            @Value("${menu.main.name}") String name,
            @Value("${menu.main.title.ru}") String title,
            @Value("${menu.main.sections.ru}") String config,
            @Value("${menu.main.max_buttons_per_row}") int maxButtonsPerRow
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
    public boolean hasDataChanged(UserData userData) {
        return isLocaleChanged(userData);
    }
}
package com.github.kegszool.menu.base;

import com.github.kegszool.LocalizationService;
import com.github.kegszool.menu.service.MenuUpdaterService;
import com.github.kegszool.user.dto.UserData;
import com.github.kegszool.user.menu.UserDataDependentBaseMenu;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Value;

import java.util.List;
import java.util.Collections;

@Component
public class MainMenu extends UserDataDependentBaseMenu  {

    @Value("${menu.main.name}")
    private String NAME;

    @Value("${menu.main.title.ru}")
    private String TITLE;

    @Value("${menu.main.sections.ru}")
    private String MENU_SECTIONS_CONFIG;

    @Value("${menu.main.max_buttons_per_row}")
    private int MAX_BUTTONS_PER_ROW;

    public MainMenu(
            MenuUpdaterService menuUpdaterService,
            LocalizationService localizationService
    ) {
        super(menuUpdaterService, null, localizationService);
    }

    @Override
    protected String getSectionsConfig() {
        return MENU_SECTIONS_CONFIG;
    }

    @Override
    protected int getMaxButtonsPerRow() {
        return MAX_BUTTONS_PER_ROW;
    }

    @Override
    protected List<String> getFullWidthSections() {
        return Collections.emptyList();
    }

    @Override
    public String getTitle() {
        return TITLE;
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public boolean hasDataChanged(UserData userData) {
//        String currentLocale = localizationService.getCurrentLocale();
//        return !userData.getLocale().equals(currentLocale) //TODO: dummy
        return true;
    }
}
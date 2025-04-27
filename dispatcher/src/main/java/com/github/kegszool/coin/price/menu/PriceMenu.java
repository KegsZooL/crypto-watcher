package com.github.kegszool.coin.price.menu;

import com.github.kegszool.LocalizationService;
import com.github.kegszool.menu.service.MenuUpdaterService;
import com.github.kegszool.user.dto.UserData;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Value;
import com.github.kegszool.user.menu.UserDataDependentBaseMenu;

import java.util.List;
import java.util.Collections;

@Component
public class PriceMenu extends UserDataDependentBaseMenu {

    @Value("${menu.price_snapshot.name}")
    private String NAME;

    @Value("${menu.price_snapshot.title.ru}")
    private String TITLE;

    @Value("${menu.price_snapshot.sections.ru}")
    private String MENU_SECTIONS_CONFIG;

    @Value("${menu.price_snapshot.max_buttons_per_row}")
    private int MAX_BUTTONS_PER_ROW;

    public PriceMenu(
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
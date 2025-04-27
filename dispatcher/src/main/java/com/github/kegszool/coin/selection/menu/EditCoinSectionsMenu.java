package com.github.kegszool.coin.selection.menu;

import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Value;

import com.github.kegszool.user.dto.UserData;
import com.github.kegszool.LocalizationService;
import com.github.kegszool.menu.service.MenuUpdaterService;
import com.github.kegszool.user.menu.UserDataDependentBaseMenu;

import java.util.List;
import java.util.Collections;

@Component
public class EditCoinSectionsMenu extends UserDataDependentBaseMenu {

    @Value("${menu.edit_coin_sections.max_buttons_per_row}")
    private int MAX_BUTTONS_PER_ROW;

    @Value("${menu.edit_coin_sections.sections.ru}")
    private String SECTIONS_CONFIG;

    @Value("${menu.edit_coin_sections.title.ru}")
    private String TITLE;

    @Value("${menu.edit_coin_sections.name}")
    private String NAME;

    public EditCoinSectionsMenu(
            MenuUpdaterService menuUpdaterService,
            LocalizationService localizationService
    ) {
        super(menuUpdaterService, null, localizationService);
    }

    @Override
    protected List<String> getFullWidthSections() {
        return Collections.emptyList();
    }

    @Override
    protected int getMaxButtonsPerRow() {
        return MAX_BUTTONS_PER_ROW;
    }

    @Override
    protected String getSectionsConfig() {
        return SECTIONS_CONFIG;
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
        return true;
    }
}
package com.github.kegszool.user.menu;

import jakarta.annotation.Nullable;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;

import com.github.kegszool.menu.base.BaseMenu;
import com.github.kegszool.menu.util.SectionBuilder;
import com.github.kegszool.menu.service.MenuUpdaterService;

import com.github.kegszool.user.dto.UserData;
import com.github.kegszool.LocalizationService;

@Component
public abstract class UserDataDependentBaseMenu extends BaseMenu implements UserDataDependentMenu {

    private final SectionBuilder sectionBuilder;
    private final MenuUpdaterService menuUpdaterService;
    protected final LocalizationService localizationService;

    @Autowired
    public UserDataDependentBaseMenu(
            MenuUpdaterService menuUpdaterService,
            @Nullable SectionBuilder sectionBuilder,
            LocalizationService localizationService
    ) {
        this.menuUpdaterService = menuUpdaterService;
        this.sectionBuilder = sectionBuilder;
        this.localizationService = localizationService;
    }

    @PostConstruct
    private void registerMenu() {
        menuUpdaterService.registerUserDataDependentMenu(getName(), this);
    }

    @Override
    public void updateMenu(UserData userData) {
        localizationService.setCurrentLocale("en"); //TODO: dummy
        if (sectionBuilder != null) {
        	String config = sectionBuilder.buildSectionsConfig(userData);
        	updateSections(config, true);
        } else {
            updateSections(getSectionsConfig(), true);
        }
    }

    @Override
    public abstract boolean hasDataChanged(UserData userData);
}
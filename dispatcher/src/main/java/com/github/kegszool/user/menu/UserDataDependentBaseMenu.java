package com.github.kegszool.user.menu;

import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;

import com.github.kegszool.user.dto.UserData;
import com.github.kegszool.menu.base.BaseMenu;
import com.github.kegszool.menu.util.SectionBuilder;
import com.github.kegszool.menu.service.MenuUpdaterService;

@Component
public abstract class UserDataDependentBaseMenu extends BaseMenu implements UserDataDependentMenu {

    private final SectionBuilder sectionBuilder;
    private final MenuUpdaterService menuUpdaterService;

    @Autowired
    public UserDataDependentBaseMenu(
            MenuUpdaterService menuUpdaterService,
            SectionBuilder sectionBuilder
    ) {
        this.menuUpdaterService = menuUpdaterService;
        this.sectionBuilder = sectionBuilder;
    }

    @PostConstruct
    private void registerMenu() {
        menuUpdaterService.registerUserDataDependentMenu(getName(), this);
    }

    @Override
    public void updateMenu(UserData userData) {
        String config = sectionBuilder.buildSectionsConfig(userData);
        updateSections(config, true);
    }

    @Override
    public abstract boolean hasDataChanged(UserData userData);
}
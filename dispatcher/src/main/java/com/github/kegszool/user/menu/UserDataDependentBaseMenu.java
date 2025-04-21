package com.github.kegszool.user.menu;

import com.github.kegszool.menu.base.BaseMenu;
import com.github.kegszool.menu.service.MenuUpdaterService;
import com.github.kegszool.menu.util.SectionBuilder;
import com.github.kegszool.user.dto.UserData;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public abstract class UserDataDependentBaseMenu extends BaseMenu implements UserDataDependentMenu {

    private final MenuUpdaterService menuUpdaterService;
    private final SectionBuilder sectionBuilder;

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
}
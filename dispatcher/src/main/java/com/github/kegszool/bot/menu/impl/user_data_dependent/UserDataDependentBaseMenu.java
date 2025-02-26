package com.github.kegszool.bot.menu.impl.user_data_dependent;

import com.github.kegszool.bot.menu.BaseMenu;
import com.github.kegszool.bot.menu.service.managment.MenuUpdaterService;
import com.github.kegszool.bot.menu.service.section.builder.SectionBuilder;
import com.github.kegszool.messaging.dto.database_entity.UserData;
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
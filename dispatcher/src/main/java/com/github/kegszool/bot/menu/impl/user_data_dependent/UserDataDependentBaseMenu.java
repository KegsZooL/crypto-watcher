package com.github.kegszool.bot.menu.impl.user_data_dependent;

import com.github.kegszool.bot.menu.BaseMenu;
import com.github.kegszool.bot.menu.service.MenuUpdaterService;
import com.github.kegszool.messaging.dto.database_entity.UserData;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public abstract class UserDataDependentBaseMenu extends BaseMenu implements UserDataDependentMenu {

    private final MenuUpdaterService menuUpdaterService;

    @Autowired
    public UserDataDependentBaseMenu(MenuUpdaterService menuUpdaterService) {
        this.menuUpdaterService = menuUpdaterService;
    }

    @PostConstruct
    private void registerMenu() {
        menuUpdaterService.registerUserDataDependentMenu(getName(), this);
    }

    @Override
    public abstract void updateMenu(UserData userData);
}
package com.github.kegszool.menu.base.main;

import com.github.kegszool.menu.base.BaseMenu;
import com.github.kegszool.menu.service.MenuRegistry;
import com.github.kegszool.menu.service.MenuUpdaterService;
import com.github.kegszool.user.messaging.dto.UserData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class UserMenuFactory {

    private final ApplicationContext context;
    private final MenuRegistry menuRegistry;
    private final MenuUpdaterService updaterService;

    @Autowired
    public UserMenuFactory(ApplicationContext context,
                           MenuRegistry menuRegistry,
                           MenuUpdaterService updaterService) {
        this.context = context;
        this.menuRegistry = menuRegistry;
        this.updaterService = updaterService;
    }

//    public BaseMenu createUserMenu(String menuName, Long telegramId, UserData userData) {
////        BaseMenu prototypeMenu = (BaseMenu) menuRegistry.getMenu(menuName);
////
////        BaseMenu userMenu = context.getBean(prototypeMenu.getClass());
////        userMenu.updateMenu(userData);
////        updaterService.registerMenu(menuName, telegramId, userMenu);
////        return userMenu;
//    }
}
package com.github.kegszool.bot.menu.service;

import com.github.kegszool.bot.menu.BaseMenu;
import com.github.kegszool.bot.menu.Menu;
import com.github.kegszool.exception.bot.menu.configuration.sections.NotSupportedUpdateMenuSectionsConfigException;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Log4j2
public class MenuUpdaterService {

    private final MenuRegistry menuRegistry;

    @Autowired
    public MenuUpdaterService(MenuRegistry menuRegistry) {
        this.menuRegistry = menuRegistry;
    }

    public void updateMenuSections(String menuName, String sectionsConfig) {
        Menu menu = menuRegistry.getMenu(menuName);
        if (menu instanceof BaseMenu baseMenu) {
            baseMenu.updateSections(sectionsConfig);
        } else {
            handleNotSupportedUpdatesSections(menuName);
        }
    }

    private void handleNotSupportedUpdatesSections(String menuName) {
        log.error("Menu \"{}\" does not support section update", menuName);
        throw new NotSupportedUpdateMenuSectionsConfigException(menuName);
    }
}
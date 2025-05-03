package com.github.kegszool.menu.service;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import com.github.kegszool.menu.base.BaseMenu;
import com.github.kegszool.user.messaging.dto.UserData;
import com.github.kegszool.localization.LocalizationService;

@Log4j2
@Service
public class MenuUpdaterService {

    private final LocalizationService localizationService;
    private final MenuRegistry menuRegistry;

    @Autowired
    public MenuUpdaterService(
            LocalizationService localizationService,
            MenuRegistry menuRegistry
    ) {
        this.menuRegistry = menuRegistry;
        this.localizationService = localizationService;
    }

    public void updateMenus(UserData userData, String chatId) {
        menuRegistry.getNameToChatMenu().forEach((menuName, userMenu) -> {
            BaseMenu menu = userMenu.get(chatId);
            if (menu.hasDataChanged(userData, chatId)) {
                menu.updateMenu(userData, chatId);
            }
        });
        String locale = userData.getUserPreference().interfaceLanguage();
        localizationService.setLocale(chatId, locale);
    }

    public void changeKeyboard(String config, String menuName, String chatId) {
        menuRegistry.getMenu(menuName, chatId).changeMenuKeyboard(config, chatId);
    }
}
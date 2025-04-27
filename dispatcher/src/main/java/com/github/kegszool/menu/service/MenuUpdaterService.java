package com.github.kegszool.menu.service;

import com.github.kegszool.LocalizationService;
import com.github.kegszool.menu.base.BaseMenu;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.kegszool.user.dto.UserData;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Log4j2
@Service
public class MenuUpdaterService {

    private final Map<String, BaseMenu> nameToMenu = new ConcurrentHashMap<>();
    private final LocalizationService localizationService;

    @Autowired
    public MenuUpdaterService(LocalizationService localizationService) {
        this.localizationService = localizationService;
    }

    public void registerMenu(String menuName, BaseMenu menu) {
        nameToMenu.put(menuName, menu);
    }

    public void updateMenus(UserData userData) {
        localizationService.setCurrentLocale("en"); //TODO: dummy
        nameToMenu.values().forEach(menu -> {
            if (menu.hasDataChanged(userData)) {
                menu.updateMenu(userData);
            }
        });
    }
}
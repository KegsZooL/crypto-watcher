package com.github.kegszool.bot.menu.service.managment;

import com.github.kegszool.bot.menu.Menu;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

@Log4j2
@Service
public class MenuRegistry {

    @Value("${menu.main.name}")
    private String DEFAULT_MENU_NAME;

    private final Map<String, Menu> menus;

    @Autowired
    public MenuRegistry(List<Menu> menuList) {
        this.menus = menuList.stream().collect(Collectors.
                toMap(Menu::getName, menu -> menu));
        logRegisteredMenus();

    }

    private void logRegisteredMenus() {
        String formattedMenus = menus.values().stream()
                        .map(entry -> entry.getClass().getSimpleName())
                        .collect(Collectors.joining(", "));
            log.info("Registered {} menus: [{}]", menus.size(), formattedMenus);
    }

    public Menu getMenu(String menuName) {
        return menus.getOrDefault(menuName, menus.get(DEFAULT_MENU_NAME));
    }

    public boolean isContained(String menuName) {
        return menus.containsKey(menuName);
    }
}
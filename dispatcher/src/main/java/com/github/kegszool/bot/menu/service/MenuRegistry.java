package com.github.kegszool.bot.menu.service;

import com.github.kegszool.bot.menu.Menu;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@Log4j2
public class MenuRegistry {

    @Value("${menu.name[4].main}")
    private String DEFAULT_MENU;

    private final Map<String, Menu> menus;

    @Autowired
    public MenuRegistry(List<Menu> menuList) {
        this.menus = menuList.stream().collect(Collectors.
                toMap(Menu::getName, menu -> menu));
        logRegistredMenus();
    }

    private void logRegistredMenus() {
        String formattedMenus = menus.entrySet().stream()
                        .map(entry -> entry.getValue().getClass().getSimpleName())
                        .collect(Collectors.joining(", "));
            log.info("Registered {} menus: [{}]", menus.size(), formattedMenus);
    }

    public Menu getMenu(String menuName) {
        return menus.getOrDefault(menuName, menus.get(DEFAULT_MENU));
    }

    public boolean isContained(String menuName) {
        return menus.containsKey(menuName);
    }
}

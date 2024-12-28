package com.github.kegszool.menu;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class MenuRegistry {

    @Value("${menu.pages[4].main}")
    private String DEFAULT_MENU;

    private final Map<String, Menu> menus;

    @Autowired
    public MenuRegistry(List<Menu> menuList) {
        this.menus = menuList.stream().collect(Collectors.
                toMap(Menu::getPageName, menu -> menu));
    }

    public Menu getMenu(String menuName) {
        return menus.getOrDefault(menuName, menus.get(DEFAULT_MENU));
    }

    public boolean isContained(String menuName) {
        return menus.containsKey(menuName);
    }
}

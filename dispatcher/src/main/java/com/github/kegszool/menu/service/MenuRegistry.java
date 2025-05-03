package com.github.kegszool.menu.service;

import com.github.kegszool.menu.base.BaseMenu;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import com.github.kegszool.menu.base.Menu;

@Getter
@Log4j2
@Service
public class MenuRegistry {

    private final Map<String, Map<String, BaseMenu>> nameToChatMenu = new ConcurrentHashMap<>();
    private final Map<String, BaseMenu> menuNameToInstance;

    @Autowired
    public MenuRegistry(List<BaseMenu> templates) {
        this.menuNameToInstance = templates.stream()
                .collect(Collectors.toMap(Menu::getName, menu -> menu));
    }

    public void registerMenu(String menuName, String chatId) {
        BaseMenu menu = menuNameToInstance.get(menuName);
        if (menu == null) {
            log.warn("Trying to register unknown menu: '{}'", menuName);
            return;
        }

        nameToChatMenu
                .computeIfAbsent(menuName, k -> new ConcurrentHashMap<>())
                .putIfAbsent(chatId, menu);

        log.info("Registered '{}' for chat id: '{}'", menuName, chatId);
    }

    public BaseMenu getMenu(String menuName, String chatId) {
        return nameToChatMenu.get(menuName).get(chatId);
    }

    public boolean isContained(String menuName, String chatId) {
        return nameToChatMenu.getOrDefault(menuName, Map.of()).containsKey(chatId);
    }
}
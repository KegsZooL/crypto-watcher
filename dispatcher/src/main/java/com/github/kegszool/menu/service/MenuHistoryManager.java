package com.github.kegszool.menu.service;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;

import java.util.Map;
import java.util.Deque;
import java.util.LinkedList;
import java.util.concurrent.ConcurrentHashMap;

@Log4j2
@Service
public class MenuHistoryManager {

    @Value("${menu.main.name}")
    private String DEFAULT_MENU_NAME;

    private final Map<String, Deque<String>> menuHistory = new ConcurrentHashMap<>();

    public void recordMenu(String chatId, String menuName) {
         menuHistory.computeIfAbsent(chatId, key -> new LinkedList<>())
                   .push(menuName);
         logHistory(chatId, "Menu added to history: " + menuName);
    }

    public String removeMenu(String chatId) {
        Deque<String> stack = menuHistory.get(chatId);
        if (stack == null || stack.size() <= 1) {
            return DEFAULT_MENU_NAME;
        }
        String removedMenuName = stack.pop();
        String currentMenuName = stack.peek();

        logHistory(chatId, "Menu removed from history: " + removedMenuName);
        return currentMenuName != null ? currentMenuName : DEFAULT_MENU_NAME;
    }

    private void logHistory(String chatId, String msg) {
        Deque<String> stack = menuHistory.get(chatId);
        String stackState = stack != null ? stack.toString() : "[]";

        log.info("{} --> Current history menu stack for chat \"{}\": {}",
                msg, chatId, stackState);
    }
}
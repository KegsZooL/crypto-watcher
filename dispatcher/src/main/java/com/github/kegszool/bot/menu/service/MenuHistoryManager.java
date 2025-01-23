package com.github.kegszool.bot.menu.service;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Deque;
import java.util.LinkedList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
@Log4j2
public class MenuHistoryManager {

    @Value("${menu.main.name}")
    private String DEFAULT_MENU_NAME;

    private final Map<String, Deque<String>> menuHistory = new ConcurrentHashMap<>();

    public void recordMenu(String chatId, String menuName) {
         Deque<String> stack = menuHistory.computeIfAbsent(chatId, key -> new LinkedList<>());
         stack.push(menuName);
         logMenuOperation(chatId, "Menu added to history: " + menuName);
    }

    public String removeMenu(String chatId) {
        Deque<String> stack = menuHistory.get(chatId);
        if(stack == null || stack.size() <= 1) {
            return DEFAULT_MENU_NAME;
        }
        String removedMenuName = stack.pop();
        String currentMenuName = stack.peek();

        logMenuOperation(chatId, "Menu removed from history: " + removedMenuName);
        return currentMenuName != null ? currentMenuName : DEFAULT_MENU_NAME;
    }

    private void logMenuOperation(String chatId, String msg) {
        Deque<String> stack = menuHistory.get(chatId);
        String stackState = stack != null ? stack.toString() : "[]";

        log.info("{} --> Current history menu stack for chat \"{}\": {}",
                msg, chatId, stackState);
    }
}
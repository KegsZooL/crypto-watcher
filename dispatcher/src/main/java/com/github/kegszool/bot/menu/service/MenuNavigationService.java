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
public class MenuNavigationService {

    private final Map<String, Deque<String>> menuHistory = new ConcurrentHashMap<>();

    @Value("${menu.pages[4].main}")
    private String DEFAULT_MENU_NAME;

    public void pushMenu(String chatId, String menuName) {
        menuHistory.computeIfAbsent(chatId, key -> new LinkedList<>()).push(menuName);
    }

    public String popMenu(String chatId) {
        Deque<String> stack = menuHistory.get(chatId);
        if(stack == null || stack.size() <= 1) {
            return DEFAULT_MENU_NAME;
        }
        stack.pop();
        return stack.peek();
    }
}
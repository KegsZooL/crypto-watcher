package com.github.kegszool.menu;

import org.springframework.stereotype.Component;

import java.util.Deque;
import java.util.LinkedList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class MenuNavigationService {

    private final Map<Long, Deque<String>> menuHistory = new ConcurrentHashMap<>();

    public void pushMenu(Long chatId, String menuName) {
        menuHistory.computeIfAbsent(chatId, key -> new LinkedList<>()).push(menuName);
    }

    public String popMenu(Long chatId) {
        Deque<String> stack = menuHistory.get(chatId);
        if(stack == null || stack.size() <= 1) {
            return "main_menu";
        }
        stack.pop();
        return stack.peek();
    }
}
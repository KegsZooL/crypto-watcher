package com.github.kegszool.menu.service;

import java.util.Map;
import java.util.List;
import java.util.Deque;
import java.util.LinkedList;
import java.util.stream.Collectors;

import lombok.extern.log4j.Log4j2;
import com.github.kegszool.menu.CalledMenu;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;

@Log4j2
@Component
public class CalledMenuManager {

    private final Map<String, Deque<String>> nameToMenuSequence;
    private final MenuHistoryManager historyManager;

    @Autowired
    public CalledMenuManager(List<CalledMenu> calledMenus, MenuHistoryManager historyManager) {
        this.nameToMenuSequence = calledMenus.stream()
                .collect(Collectors.toMap(
                        CalledMenu::getName,
                        menu -> new LinkedList(menu.getMenuSequence())));
        this.historyManager = historyManager;
    }

    public void applySequence(String chatId, String menuName) {
        Deque<String> sequence = nameToMenuSequence.get(menuName);

        if  (sequence == null) {
            log.error("No menu sequence found for menu name: {}", menuName);
            return;
        }
        historyManager.setQueue(chatId, new LinkedList<>(sequence));
    }
}
package com.github.kegszool.user;

import com.github.kegszool.menu.service.MenuRegistry;
import com.github.kegszool.user.messaging.UpsertUserSender;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;

import com.github.kegszool.messaging.util.MessageUtils;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import org.telegram.telegrambots.meta.api.objects.Update;

@Log4j2
@Component
public class UserInitializationService {

    private final MessageUtils messageUtils;
    private final MenuRegistry menuRegistry;
    private final UpsertUserSender upsertUserSender;

    private final Set<String> verifiedChats = ConcurrentHashMap.newKeySet();

    @Autowired
    public UserInitializationService(
            MessageUtils messageUtils,
            MenuRegistry menuRegistry,
            UpsertUserSender upsertUserSender
    ) {
        this.messageUtils = messageUtils;
        this.menuRegistry = menuRegistry;
        this.upsertUserSender = upsertUserSender;
    }

    public void initializeIfFirstInteraction(Update update) {
        String chatId = messageUtils.extractChatId(update);
        if (verifiedChats.add(chatId)) {
            registerAllMenusForChat(chatId);
            upsertUserSender.send(update);
        }
    }

    private void registerAllMenusForChat(String chatId) {
        menuRegistry.getMenuNameToInstance().forEach((menuName, menuInstance) -> {
            menuRegistry.registerMenu(menuName, chatId);
            menuInstance.initializeMenuForChat(chatId);
        });
        log.info("All menus registered and initialized for chat id: '{}'", chatId);
    }
}
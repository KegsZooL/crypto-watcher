package com.github.kegszool.notification;

import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class NotificationContextBuffer {

    private final Map<Long, String> selectedCoin = new ConcurrentHashMap<>();
    private final Map<Long, Boolean> notificationType = new ConcurrentHashMap<>();

    public void setCoin(Long chatId, String coin) {
        selectedCoin.put(chatId, coin);
    }

    public Optional<String> getCoin(Long chatId) {
        return Optional.ofNullable(selectedCoin.get(chatId));
    }

    public void setType(Long chatId, boolean isRecurring) {
        notificationType.put(chatId, isRecurring);
    }

    public Optional<Boolean> getType(Long chatId) {
        return Optional.ofNullable(notificationType.get(chatId));
    }

    public void clear(Long chatId) {
        notificationType.remove(chatId);
        selectedCoin.remove(chatId);
    }
}
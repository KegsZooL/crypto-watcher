package com.github.kegszool.coin.price.model;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

@Log4j2
@Component
public class PriceSnapshotCache {

    private final Map<String, PriceSnapshot> chatToSnapshot = new ConcurrentHashMap<>();

    public void save(String chatId, PriceSnapshot snapshot) {
        chatToSnapshot.put(chatId, snapshot);
        log.info("Saved the price snapshot for coin: '{}' | Chat id: {}", snapshot.getName(),chatId);
    }

    public PriceSnapshot get(String chatId) {
        PriceSnapshot snapshot = chatToSnapshot.get(chatId);
        log.info("Extracted snapshot for coin: '{}' | Chat id: '{}'", snapshot.getName(), chatId);
        return snapshot;
    }
}
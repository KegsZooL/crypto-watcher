package com.github.kegszool.coin.price.model;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import lombok.extern.log4j.Log4j2;

import org.springframework.stereotype.Component;

@Log4j2
@Component
public class PriceBuffer {

    private final Map<String, CoinPrice> chatIdSnapshotMapping = new ConcurrentHashMap<>();

    public void saveSnapshot(String chatId, CoinPrice snapshot) {
        chatIdSnapshotMapping.put(chatId, snapshot);
        log.info("Saved snapshot for chat_id \"{}\". Snapshot: {}", chatId, snapshot);
    }

    public CoinPrice getSnapshot(String chatId) {
        return chatIdSnapshotMapping.get(chatId);
    }
}
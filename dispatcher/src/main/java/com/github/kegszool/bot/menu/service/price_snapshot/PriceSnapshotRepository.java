package com.github.kegszool.bot.menu.service.price_snapshot;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import lombok.extern.log4j.Log4j2;

import com.github.kegszool.messaging.dto.command_entity.CoinPriceSnapshot;
import org.springframework.stereotype.Repository;

@Log4j2
@Repository
public class PriceSnapshotRepository {

    private final Map<String, CoinPriceSnapshot> chatIdSnapshotMapping = new ConcurrentHashMap<>();

    public void saveSnapshot(String chatId, CoinPriceSnapshot snapshot) {
        chatIdSnapshotMapping.put(chatId, snapshot);
        log.info("Saved snapshot for chat_id \"{}\". Snapshot: {}", chatId, snapshot);
    }

    public CoinPriceSnapshot getSnapshot(String chatId) {
        return chatIdSnapshotMapping.get(chatId);
    }
}
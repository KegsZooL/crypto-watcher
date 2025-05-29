package com.github.kegszool.websocket.util;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Component;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class WebSocketSubscriptionTracker {

    private final Map<String, AtomicInteger> activeSubscriptions = new ConcurrentHashMap<>();

    public synchronized boolean increment(String instId) {
        return activeSubscriptions.compute(instId, (key, counter) -> {
            if (counter == null) return new AtomicInteger(1);
            counter.incrementAndGet();
            return counter;
        }).get() == 1;
    }

    public synchronized void decrement(String instId) {
        AtomicInteger counter = activeSubscriptions.get(instId);
        if (counter == null) return;

        int count = counter.decrementAndGet();
        if (count <= 0) {
            activeSubscriptions.remove(instId);
        }
    }
}
package com.github.kegszool.websocket;

import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class SubscriptionTracker {

    private final Map<String, AtomicInteger> activeSubscriptions = new ConcurrentHashMap<>();

    public synchronized boolean increment(String instId) {
        return activeSubscriptions.compute(instId, (key, counter) -> {
            if (counter == null) return new AtomicInteger(1);
            counter.incrementAndGet();
            return counter;
        }).get() == 1;
    }

    public synchronized boolean decrement(String instId) {
        AtomicInteger counter = activeSubscriptions.get(instId);
        if (counter == null) return false;

        int count = counter.decrementAndGet();
        if (count <= 0) {
            activeSubscriptions.remove(instId);
            return true;
        }
        return false;
    }

    public int getCount(String instId) {
        AtomicInteger count = activeSubscriptions.get(instId);
        return count == null ? 0 : count.get();
    }
}
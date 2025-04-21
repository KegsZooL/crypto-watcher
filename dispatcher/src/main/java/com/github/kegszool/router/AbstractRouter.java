package com.github.kegszool.router;

import com.github.kegszool.messaging.dto.HandlerResult;
import jakarta.annotation.PreDestroy;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Service
public abstract class AbstractRouter<T, H, K> {

    private final List<H> handlers;
    private final ExecutorService executorService;

    protected AbstractRouter(List<H> handlers) {
        this.handlers = handlers;
        this.executorService = Executors.newFixedThreadPool(2);
    }

    public HandlerResult routeAndHandle(T data, K key) {
        ExecutorService executor = Executors.newCachedThreadPool();
        return CompletableFuture.supplyAsync(() -> {
                return handlers.parallelStream()
                        .filter(handler -> canHandle(handler, key))
                        .findFirst()
                        .orElseThrow(() -> processMissingHandler(data, key));
                }, executor)
                .thenApplyAsync(handler -> handle(handler, data), executor)
                .join();
    }

    protected abstract boolean canHandle(H handler, K key);

    protected abstract HandlerResult handle(H handler, T data);

    protected abstract HandlerNotFoundException processMissingHandler(T input, K key);

    @PreDestroy
    private void closeThreadPool() {
        executorService.shutdownNow();
        try {
            if (!executorService.awaitTermination(30, TimeUnit.SECONDS)) {
                executorService.shutdownNow();
                Thread.currentThread().interrupt();
            }
        } catch (InterruptedException ex) {
            executorService.shutdownNow();
        }
    }
}
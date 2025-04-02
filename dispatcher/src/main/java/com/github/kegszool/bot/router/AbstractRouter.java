package com.github.kegszool.bot.router;

import com.github.kegszool.bot.handler.HandlerResult;
import com.github.kegszool.exception.bot.handler.HandlerNotFoundException;
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
        this.executorService = Executors.newFixedThreadPool(10);
    }

    public HandlerResult routeAndHandle(T data, K key) {
        ExecutorService executor = Executors.newCachedThreadPool();
        return CompletableFuture.supplyAsync(() -> {
                return handlers.parallelStream()
                        .filter(handler -> canHandle(handler, key))
                        .findFirst()
                        .orElseThrow(() -> proccessMissingHandler(data, key));
                }, executor)
                .thenApplyAsync(handler -> handle(handler, data), executor)
                .join();
    }

    protected abstract boolean canHandle(H handler, K key);

    protected abstract HandlerResult handle(H handler, T data);

    protected abstract HandlerNotFoundException proccessMissingHandler(T input, K key);

    @PreDestroy
    private void closeTreadPool() {
        executorService.shutdownNow();
        try {
            if (!executorService.awaitTermination(60, TimeUnit.SECONDS)) {
                executorService.shutdownNow();
            }
        } catch (InterruptedException ex) {
            executorService.shutdownNow();
        }
    }
}
package com.github.kegszool.router;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.CompletableFuture;

import lombok.extern.log4j.Log4j2;
import jakarta.annotation.PreDestroy;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import com.github.kegszool.NotSupportedMessageBuilder;
import com.github.kegszool.messaging.dto.HandlerResult;
import org.telegram.telegrambots.meta.api.objects.Update;
import com.github.kegszool.update.exception.UpdateHandlerNotFoundException;

@Log4j2
@Service
public abstract class AbstractRouter<T, H, K> {

    private final List<H> handlers;
    private final ExecutorService executorService;

    @Autowired
    private NotSupportedMessageBuilder notSupportedMessageBuilder;

    @Autowired
    protected AbstractRouter(List<H> handlers) {
        this.handlers = handlers;
        this.executorService = Executors.newFixedThreadPool(4);
    }

    public HandlerResult routeAndHandle(T data, K key) {
        return CompletableFuture.supplyAsync(() -> handlers.stream()
                .filter(handler -> canHandle(handler, key))
                .findFirst()
                .orElseThrow(() -> processMissingHandler(data, key)), executorService)
                .thenApplyAsync(handler -> handle(handler, data), executorService)
                .exceptionally(ex -> {
                    if (ex.getCause() instanceof UpdateHandlerNotFoundException) {
                        return notSupportedMessageBuilder.build((Update)data);
                    }
                    log.error("Error handling message", ex);
                    return new HandlerResult.NoResponse();
                })
                .join();
    }

    protected abstract boolean canHandle(H handler, K key);

    protected abstract HandlerResult handle(H handler, T data);

    protected abstract HandlerNotFoundException processMissingHandler(T input, K key);

    @PreDestroy
    private void closeThreadPool() {
        executorService.shutdownNow();
        try {
            if (!executorService.awaitTermination(60, TimeUnit.SECONDS)) {
                executorService.shutdownNow();
                Thread.currentThread().interrupt();
            }
        } catch (InterruptedException ex) {
            executorService.shutdownNow();
        }
    }
}
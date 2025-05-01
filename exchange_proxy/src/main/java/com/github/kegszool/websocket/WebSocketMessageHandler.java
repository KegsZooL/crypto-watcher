package com.github.kegszool.websocket;

import com.github.kegszool.utils.JsonParser;
import com.github.kegszool.websocket.checker.NotificationCheckerService;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;

@Log4j2
@Component
public class WebSocketMessageHandler {

    private final JsonParser jsonParser;
    private final NotificationCheckerService notificationChecker;

    @Autowired
    public WebSocketMessageHandler(
            JsonParser jsonParser,
            NotificationCheckerService notificationChecker
    ) {
        this.jsonParser = jsonParser;
        this.notificationChecker = notificationChecker;
    }

    public void handle(String message) {
        log.info(message);
    }
}
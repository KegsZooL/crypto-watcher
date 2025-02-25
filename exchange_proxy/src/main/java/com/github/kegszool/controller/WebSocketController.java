package com.github.kegszool.controller;

import com.github.kegszool.websocket.OKXWebSocketClient;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Log4j2
@Service
public class WebSocketController {

    private final OKXWebSocketClient okxWebSocketClient;

    @Autowired
    public WebSocketController(OKXWebSocketClient okxWebSocketClient) {
        this.okxWebSocketClient = okxWebSocketClient;
    }

    public void handleRequest(String request) {

    }
}
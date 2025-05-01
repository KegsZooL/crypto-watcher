package com.github.kegszool.websocket.connection;

public interface WebSocketConnector<T> {
    void connect(T key);
    void disconnect(T key);
}
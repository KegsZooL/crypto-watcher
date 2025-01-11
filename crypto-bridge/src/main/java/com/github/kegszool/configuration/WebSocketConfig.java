package com.github.kegszool.configuration;

import com.github.kegszool.OKXWebSocketClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.URI;
import java.net.URISyntaxException;

@Configuration
public class WebSocketConfig {

    @Bean
    public OKXWebSocketClient okxWebSocketClient() throws URISyntaxException {
        URI serverURI = new URI("wss://ws.okx.com:8443/ws/v5/public");
        return new OKXWebSocketClient(serverURI);
    }
}
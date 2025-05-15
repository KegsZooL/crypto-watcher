package com.github.kegszool.notificaiton.creation;

import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.annotation.Autowired;

import com.github.kegszool.messaging.dto.CoinDto;
import com.github.kegszool.messaging.dto.NotificationDto;
import com.github.kegszool.messaging.dto.service.ServiceMessage;

import com.github.kegszool.coin.util.CoinPriceFetcher;
import com.github.kegszool.coin.util.CoinExistenceChecker;

import com.github.kegszool.notificaiton.NotificationProducer;
import com.github.kegszool.websocket.util.WebSocketSubscriptionTracker;
import com.github.kegszool.websocket.connection.OkxWebSocketConnector;

@Component
public class NotificationCreationService {
    
    private final String currencySuffix;
    
    private final CoinExistenceChecker coinExistenceChecker;
    private final CoinPriceFetcher coinPriceFetcher;
    private final NotificationProducer notificationProducer;
    private final WebSocketSubscriptionTracker subscriptionTracker;
    private final OkxWebSocketConnector webSocketConnector;

    @Autowired
    public NotificationCreationService(
            @Value("${currency_suffix}") String currencySuffix,
            CoinExistenceChecker coinExistenceChecker,
            CoinPriceFetcher coinPriceFetcher,
            NotificationProducer notificationProducer,
            WebSocketSubscriptionTracker subscriptionTracker,
            OkxWebSocketConnector webSocketConnector
    ) {
        this.currencySuffix = currencySuffix;
        this.coinExistenceChecker = coinExistenceChecker;
        this.coinPriceFetcher = coinPriceFetcher;
        this.notificationProducer = notificationProducer;
        this.subscriptionTracker = subscriptionTracker;
        this.webSocketConnector = webSocketConnector;
    }
    
    public boolean processCreationRequest(ServiceMessage<NotificationDto> serviceMessage) {

        NotificationDto notification = serviceMessage.getData();
        String fullCoinName = notification.getCoin().getName();
        String coinName = fullCoinName.replace(currencySuffix, "");
        
        if (!coinExistenceChecker.exists(coinName)) {
            return false;
        }
        double initialPrice = coinPriceFetcher.getPrice(fullCoinName);
        notification.setInitialPrice(initialPrice);
        notification.setCoin(new CoinDto(coinName));
        serviceMessage.setData(notification);
        
        notificationProducer.sendCreationRequest(serviceMessage);

        if (subscriptionTracker.increment(coinName)) {
            webSocketConnector.connect(fullCoinName);
        }
        return true;
    }
}
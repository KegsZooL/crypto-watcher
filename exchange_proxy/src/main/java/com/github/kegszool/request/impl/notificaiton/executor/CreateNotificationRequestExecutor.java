package com.github.kegszool.request.impl.notificaiton.executor;

import com.github.kegszool.messaging.dto.CoinDto;
import com.github.kegszool.messaging.dto.NotificationDto;
import com.github.kegszool.messaging.dto.service.ServiceMessage;
import com.github.kegszool.messaging.producer.ResponseProducer;
import com.github.kegszool.request.BaseRequestExecutor;
import com.github.kegszool.request.impl.coin.CoinExistenceChecker;
import com.github.kegszool.request.impl.coin.CoinPriceFetcher;
import com.github.kegszool.rest.RestCryptoController;
import com.github.kegszool.utils.JsonParser;
import com.github.kegszool.websocket.SubscriptionTracker;
import com.github.kegszool.websocket.connection.OkxWebSocketConnector;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class CreateNotificationRequestExecutor extends BaseRequestExecutor<NotificationDto, NotificationDto> {

    private final String routingKey;
    private final OkxWebSocketConnector webSocketConnector;
    private final SubscriptionTracker subscriptionTracker;
    private final CoinExistenceChecker coinExistenceChecker;
    private final CoinPriceFetcher coinPriceFetcher;
    private final ResponseProducer responseProducer;
    private final String requestRoutingKey;

    @Autowired
    public CreateNotificationRequestExecutor(
            RestCryptoController restCryptoController,
            JsonParser jsonParser,
            @Value("${spring.rabbitmq.template.routing-key.create_notification_response}") String routingKey,
            OkxWebSocketConnector webSocketConnector,
            SubscriptionTracker subscriptionTracker,
            CoinExistenceChecker coinExistenceChecker,
            CoinPriceFetcher coinPriceFetcher,
            ResponseProducer responseProducer,
            @Value("${spring.rabbitmq.template.routing-key.create_notification_request}") String requestRoutingKey
    ) {
        super(restCryptoController, jsonParser);
        this.routingKey = routingKey;
        this.coinPriceFetcher = coinPriceFetcher;
        this.webSocketConnector = webSocketConnector;
        this.subscriptionTracker = subscriptionTracker;
        this.coinExistenceChecker = coinExistenceChecker;
        this.responseProducer = responseProducer;
        this.requestRoutingKey = requestRoutingKey;
    }

    @Override
    public ServiceMessage<NotificationDto> execute(ServiceMessage<NotificationDto> serviceMessage) {

        NotificationDto notification = serviceMessage.getData();
        String coinName = notification.getCoin().getName();
        String coinNameWithoutCurrencyPrefix = coinName.replace("-USD", "");

        if (coinExistenceChecker.exists(coinNameWithoutCurrencyPrefix)) {

            double initialPrice = coinPriceFetcher.getPrice(coinName);
            notification.setInitialPrice(initialPrice);
            notification.setCoin(new CoinDto(coinNameWithoutCurrencyPrefix));
            serviceMessage.setData(notification);
            responseProducer.produce(serviceMessage, requestRoutingKey);

            if (subscriptionTracker.increment(coinName)) {
                webSocketConnector.connect(coinName);
            }
        }
        return null;
    }

    @Override
    public String getResponseRoutingKey() {
        return routingKey;
    }
}
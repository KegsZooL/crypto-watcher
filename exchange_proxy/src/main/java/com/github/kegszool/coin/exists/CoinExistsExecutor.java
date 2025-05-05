package com.github.kegszool.coin.exists;

import java.util.List;
import java.util.ArrayList;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.annotation.Autowired;

import com.github.kegszool.messaging.dto.UserDto;
import com.github.kegszool.messaging.dto.service.ServiceMessage;
import com.github.kegszool.messaging.executor.BaseRequestExecutor;
import com.github.kegszool.messaging.dto.command_entity.UserCoinData;
import com.github.kegszool.messaging.dto.command_entity.CoinExistenceResult;

import com.github.kegszool.utils.JsonParser;
import com.github.kegszool.rest.RestCryptoController;
import com.github.kegszool.coin.util.CoinExistenceChecker;

@Log4j2
@Component
public class CoinExistsExecutor extends BaseRequestExecutor<UserCoinData, CoinExistenceResult> {

    private final CoinExistenceChecker existenceChecker;
    private final String responseRoutingKey;

    @Autowired
    public CoinExistsExecutor(
            RestCryptoController restCryptoController,
            JsonParser jsonParser,

            @Value("${spring.rabbitmq.template.routing-key.check_coin_exists_response}")
            String responseRoutingKey,
            CoinExistenceChecker existenceChecker
    ) {
        super(restCryptoController, jsonParser);
        this.existenceChecker = existenceChecker;
        this.responseRoutingKey = responseRoutingKey;
    }

    @Override
    public String getResponseRoutingKey() {
        return responseRoutingKey;
    }

    @Override
    public ServiceMessage<CoinExistenceResult> execute(ServiceMessage<UserCoinData> serviceMessage) {

        UserCoinData userCoinData = serviceMessage.getData();
        List<String> coins = userCoinData.getCoinsToAdd();

        List<String> validCoins = new ArrayList<>();
        List<String> invalidCoins = new ArrayList<>();

        for (String coin : coins) {
            if (existenceChecker.exists(coin)) {
                validCoins.add(coin);
            } else {
                invalidCoins.add(coin);
            }
        }
        return createResponseMessage(validCoins, invalidCoins, serviceMessage);
    }

    private ServiceMessage<CoinExistenceResult> createResponseMessage(
            List<String> validCoins, List<String> invalidCoins, ServiceMessage<UserCoinData> serviceMessage
    ) {

        UserDto userDto = serviceMessage.getData().getUser();
        CoinExistenceResult result = new CoinExistenceResult(validCoins, invalidCoins, userDto);

        ServiceMessage<CoinExistenceResult> responseMessage = new ServiceMessage<>();
        responseMessage.setData(result);
        responseMessage.setMessageId(serviceMessage.getMessageId());
        responseMessage.setChatId(serviceMessage.getChatId());

        return responseMessage;
    }
}
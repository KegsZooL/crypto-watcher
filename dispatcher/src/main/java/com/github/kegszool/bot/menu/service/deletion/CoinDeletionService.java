package com.github.kegszool.bot.menu.service.deletion;

import com.github.kegszool.bot.menu.service.selection.SelectionStateRepository;
import com.github.kegszool.messaging.dto.database_entity.CoinDto;
import com.github.kegszool.messaging.dto.database_entity.FavoriteCoinDto;
import com.github.kegszool.messaging.dto.database_entity.UserDto;
import com.github.kegszool.messaging.dto.service.ServiceMessage;
import com.github.kegszool.messaging.producer.RequestProducerService;
import com.github.kegszool.messaging.dto.database_entity.UserData;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.User;

import java.util.Collections;
import java.util.List;

@Service
public class CoinDeletionService {

    @Value("${spring.rabbitmq.template.routing-key.delete_favorite_coin_request}")
    private String ROUTING_KEY;

    private final RequestProducerService requestProducerService;
    private final SelectionStateRepository selectionStateRepository;

    @Autowired
    public CoinDeletionService(
            RequestProducerService requestProducerService,
            SelectionStateRepository selectionStateRepository
    ) {
        this.requestProducerService = requestProducerService;
        this.selectionStateRepository = selectionStateRepository;
    }

    public void processCoinDeletion(CallbackQuery callbackQuery, String menuName) {
        UserData userData = createUserData(callbackQuery, menuName);
        produceRequest(callbackQuery, userData);
        selectionStateRepository.clearSelected(menuName);
    }

    public UserData createUserData(CallbackQuery callbackQuery, String menuName) {
        User user  = callbackQuery.getFrom();
        UserDto userDto = new UserDto(user.getId(), user.getFirstName(), user.getLastName());
        List<FavoriteCoinDto> favoriteCoins = selectionStateRepository.getSelected(menuName)
                .stream()
                .map(selectedButton -> {
                    String nameWithoutEmoji = selectedButton.getText().split(" ")[1];
                    CoinDto coin = new CoinDto(nameWithoutEmoji);
                    return new FavoriteCoinDto(userDto, coin);
                })
                .toList();
        return new UserData(userDto, favoriteCoins, Collections.emptyList());
    }

    private void produceRequest(CallbackQuery callbackQuery, UserData userData) {
        Integer messageId = callbackQuery.getMessage().getMessageId();
        ServiceMessage<UserData> serviceMessage = new ServiceMessage<>(
                messageId, callbackQuery.getId(), userData
        );
        requestProducerService.produce(ROUTING_KEY, serviceMessage);
    }
}
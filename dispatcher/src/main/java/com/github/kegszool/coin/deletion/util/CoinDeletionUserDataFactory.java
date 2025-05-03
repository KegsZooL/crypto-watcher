package com.github.kegszool.coin.deletion.util;

import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;

import com.github.kegszool.coin.dto.CoinDto;
import com.github.kegszool.coin.dto.FavoriteCoinDto;

import com.github.kegszool.user.messaging.dto.UserDto;
import com.github.kegszool.user.messaging.dto.UserData;
import com.github.kegszool.user.UserDataFactory;

import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

@Component
public class CoinDeletionUserDataFactory {

    private final UserDataFactory userDataFactory;

    @Autowired
    public CoinDeletionUserDataFactory(UserDataFactory userDataFactory) {
        this.userDataFactory = userDataFactory;
    }

    public UserData createFromSelected(CallbackQuery callbackQuery, List<InlineKeyboardButton> selected) {

        String chatId = callbackQuery.getMessage().getChatId().toString();
        User user  = callbackQuery.getFrom();
        UserDto userDto = new UserDto(user.getId(), user.getFirstName(), user.getLastName());

        List<FavoriteCoinDto> selectedCoins = new ArrayList<>(selected.size());

        for (InlineKeyboardButton button : selected) {
            String text = button.getText();
            int spaceIndex = text.indexOf(' ');
            if (spaceIndex < 0 || spaceIndex == text.length() - 1) continue;

            String coinName = text.substring(spaceIndex + 1);
            CoinDto coinDto = new CoinDto(coinName);
            selectedCoins.add(new FavoriteCoinDto(userDto, coinDto));
        }
        return userDataFactory.create(userDto, selectedCoins, Collections.emptyList(),chatId); //TODO NOTIFICATION EMPTY
    }
}
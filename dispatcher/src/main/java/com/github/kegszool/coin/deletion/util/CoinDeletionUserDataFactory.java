package com.github.kegszool.coin.deletion.util;

import com.github.kegszool.coin.dto.CoinDto;
import com.github.kegszool.coin.dto.FavoriteCoinDto;
import org.springframework.stereotype.Component;

import com.github.kegszool.user.dto.UserDto;
import com.github.kegszool.user.dto.UserData;

import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

@Component
public class CoinDeletionUserDataFactory {

    public UserData createFromSelected(CallbackQuery callbackQuery, List<InlineKeyboardButton> selected) {

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
        return new UserData(userDto, selectedCoins, Collections.emptyList());
    }
}
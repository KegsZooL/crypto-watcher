package com.github.kegszool.coin.deletion.util;

import com.github.kegszool.user.dto.UserData;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class CoinDeletionUserDataFactoryTest {

    @Mock
    CoinDeletionUserDataFactory factory;

    @Mock
    private User user;

    @Test
    void createFromSelected_shouldCreateUserDataWithSelectedCoins() {
        CallbackQuery callbackQuery = createCallbackQuery();
        List<InlineKeyboardButton> buttons = List.of(
               	new InlineKeyboardButton("✅ BTC"),
                new InlineKeyboardButton("✅ ETH"),
                new InlineKeyboardButton("Invalid")
        );

        UserData result = factory.createFromSelected(callbackQuery, buttons);

        assertEquals(2, result.getFavoriteCoins().size());
        assertEquals("BTC", result.getFavoriteCoins().get(0).getCoin().getName());
        assertEquals("ETH", result.getFavoriteCoins().get(1).getCoin().getName());
    }

    @Test
    void createFromSelected_shouldHandleEmptySelection() {
        CallbackQuery callbackQuery = createCallbackQuery();

        UserData result = factory.createFromSelected(callbackQuery, Collections.emptyList());

        assertEquals(0, result.getFavoriteCoins().size());
    }

    @Test
    void createFromSelected_shouldSkipInvalidButtons() {
        CallbackQuery callbackQuery = createCallbackQuery();
        List<InlineKeyboardButton> buttons = List.of(
             	new InlineKeyboardButton("NoSpace"),
                new InlineKeyboardButton(" "),
                new InlineKeyboardButton("✅")
        );

        UserData result = factory.createFromSelected(callbackQuery, buttons);

        assertEquals(0, result.getFavoriteCoins().size());
    }

    private CallbackQuery createCallbackQuery() {

        CallbackQuery callbackQuery = new CallbackQuery();
        callbackQuery.setFrom(user);
        return callbackQuery;
    }
}

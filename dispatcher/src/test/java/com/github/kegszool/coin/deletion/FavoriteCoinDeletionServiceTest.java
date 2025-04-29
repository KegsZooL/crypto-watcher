package com.github.kegszool.coin.deletion;

import com.github.kegszool.coin.selection.menu.BaseCoinMenu;
import com.github.kegszool.coin.deletion.service.FavoriteCoinDeletionService;
import com.github.kegszool.coin.deletion.util.CoinDeletionUserDataFactory;
import com.github.kegszool.coin.selection.util.state.MenuSelectionBuffer;
import com.github.kegszool.menu.base.Menu;
import com.github.kegszool.menu.service.MenuRegistry;
import com.github.kegszool.menu.service.MenuUpdaterService;
import com.github.kegszool.messaging.dto.service.ServiceMessage;
import com.github.kegszool.coin.dto.CoinDto;
import com.github.kegszool.coin.dto.FavoriteCoinDto;
import com.github.kegszool.messaging.producer.RequestProducerService;
import com.github.kegszool.user.dto.UserData;
import com.github.kegszool.user.dto.UserDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.message.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FavoriteCoinDeletionServiceTest {

    @Mock private MenuUpdaterService menuUpdater;
    @Mock private MenuSelectionBuffer menuSelectionBuffer;
    @Mock private RequestProducerService requestProducerService;
    @Mock private CoinDeletionUserDataFactory coinDeletionUserDataFactory;
    @Mock private MenuRegistry menuRegistry;
    @Mock private BaseCoinMenu baseCoinMenu;

    @InjectMocks
    private FavoriteCoinDeletionService service;

    @Test
    void deleteSelectedCoins_shouldDoNothingWhenNoButtonsSelected() {
        CallbackQuery callbackQuery = mock(CallbackQuery.class);
        when(menuSelectionBuffer.getSelected(any())).thenReturn(Collections.emptyList());

        service.deleteSelectedCoins(callbackQuery);
        verifyNoInteractions(requestProducerService, coinDeletionUserDataFactory, menuUpdater);
    }

    @Test
    void deleteSelectedCoins_shouldProcessSelectedCoins() {
        CallbackQuery callbackQuery = createCallbackQuery();
        User user = callbackQuery.getFrom();
        UserDto userDto = new UserDto(user.getId(), user.getFirstName(), user.getLastName());

        List<InlineKeyboardButton> selectedButtons = List.of(
                new InlineKeyboardButton("✅ BTC"),
                new InlineKeyboardButton("✅ ETH")
        );

        List<FavoriteCoinDto> selectedCoins = List.of(
                new FavoriteCoinDto(userDto, new CoinDto("BTC")),
                new FavoriteCoinDto(userDto, new CoinDto("ETH"))
        );

        UserData deletionUserData = new UserData(userDto, selectedCoins, Collections.emptyList());

        when(menuSelectionBuffer.getSelected(any())).thenReturn(selectedButtons);
        when(coinDeletionUserDataFactory.createFromSelected(callbackQuery, selectedButtons))
                .thenReturn(deletionUserData);
        when(menuRegistry.getMenu(any())).thenReturn(baseCoinMenu);

        List<FavoriteCoinDto> allCoins = List.of(
                new FavoriteCoinDto(userDto, new CoinDto("BTC")),
                new FavoriteCoinDto(userDto, new CoinDto("ETH")),
                new FavoriteCoinDto(userDto, new CoinDto("ADA"))
        );

        when(baseCoinMenu.getAllFavoriteCoins(userDto)).thenReturn(allCoins);

        service.deleteSelectedCoins(callbackQuery);

        verify(menuSelectionBuffer).removeSelected(any());
        verify(requestProducerService).produce(any(), any(ServiceMessage.class));

        ArgumentCaptor<UserData> userDataCaptor = ArgumentCaptor.forClass(UserData.class);
        verify(menuUpdater).updateMenus(userDataCaptor.capture());

        UserData remainingData = userDataCaptor.getValue();
        assertEquals(1, remainingData.getFavoriteCoins().size());
        assertEquals("ADA", remainingData.getFavoriteCoins().get(0).getCoin().getName());
    }

    @Test
    void deleteSelectedCoins_shouldHandleNonFavoriteCoinMenu() {
        CallbackQuery callbackQuery = createCallbackQuery();
        List<InlineKeyboardButton> selectedButtons = List.of(new InlineKeyboardButton("✅ BTC"));

        UserData deletionUserData = mock(UserData.class);
        when(deletionUserData.getUser()).thenReturn(mock(UserDto.class));

        when(menuSelectionBuffer.getSelected(any())).thenReturn(selectedButtons);
        when(coinDeletionUserDataFactory.createFromSelected(any(), any())).thenReturn(deletionUserData);
        when(menuRegistry.getMenu(any())).thenReturn(mock(Menu.class));

        service.deleteSelectedCoins(callbackQuery);

        verify(menuUpdater, never()).updateMenus(any());
    }


    private CallbackQuery createCallbackQuery() {

        User user = mock(User.class);

        Message message = new Message();
        message.setMessageId(456);

        CallbackQuery callbackQuery = new CallbackQuery();
        callbackQuery.setId("cb123");
        callbackQuery.setFrom(user);
        callbackQuery.setMessage(message);

        return callbackQuery;
    }
}

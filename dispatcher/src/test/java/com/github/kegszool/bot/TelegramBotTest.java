package com.github.kegszool.bot;

import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.mockito.Mockito.*;

import org.springframework.test.util.ReflectionTestUtils;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.generics.TelegramClient;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.methods.botapimethods.PartialBotApiMethod;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.github.kegszool.bot.exception.method.UnsupportedTelegramMethodTypeException;

@ExtendWith(MockitoExtension.class)
public class TelegramBotTest {

    private final String token = "some_token";

    @Mock
    private TelegramBotController controller;

    @Mock
    private TelegramClient client;

    private TelegramBot bot;

    @BeforeEach
    void setUp() {
        bot = new TelegramBot(token, controller);
        ReflectionTestUtils.setField(bot, "client", client);
    }

    @Test
    void consume_ShouldCallControllerHandleUpdate() {
        Update update = new Update();
        bot.consume(update);
        verify(controller).handleUpdate(update);
    }

    @Test
    void sendAnswerMessage_ShouldExecuteSendMessage() throws TelegramApiException {
        SendMessage msg = new SendMessage("", "");
        bot.sendAnswerMessage(msg);
        verify(client).execute(msg);
    }

    @Test
    void sendAnswerMessage_ShouldExecuteEditMessageText() throws TelegramApiException {
        EditMessageText msg = new EditMessageText("");
        bot.sendAnswerMessage(msg);
        verify(client).execute(msg);
    }

    @Test
    void sendAnswerMessage_ShouldThrowUnsupportedTelegramMethodTypeException_ForUnsupportedMethod() {
        PartialBotApiMethod<?> unsupportedMethod = mock(PartialBotApiMethod.class);
        when(unsupportedMethod.getMethod()).thenReturn("unsupported_method");

        UnsupportedTelegramMethodTypeException ex = assertThrows(
                UnsupportedTelegramMethodTypeException.class,
                () -> bot.sendAnswerMessage(unsupportedMethod)
        );
        assertEquals("unsupported_method", ex.getMessage());
    }
}
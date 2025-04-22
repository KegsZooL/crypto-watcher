package com.github.kegszool.bot;

import org.mockito.Mock;
import org.mockito.InjectMocks;
import org.mockito.MockedConstruction;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mockConstruction;
import static org.mockito.ArgumentMatchers.anyString;

import org.springframework.test.util.ReflectionTestUtils;
import org.telegram.telegrambots.meta.generics.TelegramClient;
import org.telegram.telegrambots.longpolling.TelegramBotsLongPollingApplication;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class BotRegistrationServiceTest {

    @InjectMocks
    private BotRegistrationService botRegistrationService;

    @Mock
    private TelegramBotController controller;

    @Mock
    private TelegramClient client;

    private String token = "some_token";

    private TelegramBot bot;

    @BeforeEach
    void setUp() {
        bot = new TelegramBot(token, controller);
        ReflectionTestUtils.setField(bot, "client", client);
    }

    @Test
    void should_SuccessfullyRegisterBot_WithValidToken() {
        try (MockedConstruction<TelegramBotsLongPollingApplication> mocked =
                mockConstruction(TelegramBotsLongPollingApplication.class,
                        (mock, context) ->
                        when(mock.registerBot(anyString(), any())).thenReturn(null))) {
            TelegramBot result = botRegistrationService.register(bot);
            assertEquals(bot, result);
        }
    }
}
package com.github.kegszool.coin.deletion.command;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import org.mockito.Mock;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.junit.jupiter.MockitoExtension;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.methods.botapimethods.PartialBotApiMethod;
import com.github.kegszool.coin.deletion.handler.ConfirmCoinDeletionCommandHandler;

@ExtendWith(MockitoExtension.class)
class ConfirmCoinDeletionCommandTest {

    @Mock
    private ConfirmCoinDeletionCommandHandler confirmCommandHandler;

    private final String command = "action_delete_selected";

    @Test
    void canHandleCommand_ShouldReturnTrue_WhenCallbackDataMatches() {
        ConfirmCoinDeletionCommand command = new ConfirmCoinDeletionCommand(
                this.command, confirmCommandHandler
        );
        assertTrue(command.canHandleCommand(this.command));
    }

    @Test
    void canHandleCommand_ShouldReturnFalse_WhenCallbackDataDoesNotMatch() {
        ConfirmCoinDeletionCommand command = new ConfirmCoinDeletionCommand(
                this.command, confirmCommandHandler
        );
        assertFalse(command.canHandleCommand("WRONG_COMMAND"));
    }

    @Test
    @SuppressWarnings("unchecked")
    void handleCommand_ShouldCallHandlerAndReturnResponse() {
        CallbackQuery mockCallbackQuery = mock(CallbackQuery.class);
        EditMessageText expected = mock(EditMessageText.class);

        when(confirmCommandHandler.delete(mockCallbackQuery))
                .thenReturn((PartialBotApiMethod)expected);

        ConfirmCoinDeletionCommand command = new ConfirmCoinDeletionCommand(
                this.command, confirmCommandHandler
        );
        PartialBotApiMethod<?> result = command.handleCommand(mockCallbackQuery);
        assertEquals(expected, result);
        verify(confirmCommandHandler).delete(mockCallbackQuery);
    }
}
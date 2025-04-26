package com.github.kegszool.coin.deletion.command;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
class DisplayCoinDeletionMenuTest {

    @InjectMocks
    private DisplayCoinDeletionMenu displayCoinDeletionMenu;

    @BeforeEach
    void setUp() {
        displayCoinDeletionMenu = new DisplayCoinDeletionMenu(
                "action_delete_coins", "coin_deletion_menu"
        );
    }

    @Test
    void testCanHandleCommand() {
        String validCommand = "action_delete_coins";
        String invalidCommand = "OTHER_COMMAND";

        assertTrue(displayCoinDeletionMenu.canHandleCommand(validCommand));
        assertFalse(displayCoinDeletionMenu.canHandleCommand(invalidCommand));
    }
}
package com.github.kegszool.bot.menu.service.selection.data_updater;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import com.github.kegszool.exception.bot.menu.configuration.section.InvalidButtonTextException;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;

import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
public class CoinDeletionDataUpdater implements SelectionDataUpdater {

    @Value("${menu.coin_deletion_menu.prefix.selected_coin_prefix}")
    private String SELECTED_DELETION_COIN_PREFIX;

    @Value("${menu.coin_deletion_menu.prefix.unselected_coin_prefix}")
    private String UNSELECTED_DELETION_COIN_PREFIX;

    @Value("${emoji_unicode_symbol.white_heavy_check_mark}")
    private String WHITE_HEAVY_CHECK_MARK;

    @Value("${emoji_unicode_symbol.ballot_box_with_check}")
    private String BALLOT_BOX_WITH_CHECK_MARK;

    @Override
    public void update(InlineKeyboardButton button, String currentCallbackData) {
        if (currentCallbackData.startsWith(SELECTED_DELETION_COIN_PREFIX)) {
            updateData(button, UNSELECTED_DELETION_COIN_PREFIX, SELECTED_DELETION_COIN_PREFIX, BALLOT_BOX_WITH_CHECK_MARK);
        } else {
            updateData(button, SELECTED_DELETION_COIN_PREFIX, UNSELECTED_DELETION_COIN_PREFIX, WHITE_HEAVY_CHECK_MARK);
        }
    }

    private void updateData(InlineKeyboardButton button, String newPrefix, String currentPrefix, String newEmoji) {
        String currentCallback = button.getCallbackData();
        String callbackWithoutPrefix = currentCallback.substring(currentPrefix.length());
        button.setCallbackData(newPrefix + callbackWithoutPrefix);

        String buttonText = button.getText();
        String[] words = buttonText.split(" ");

        if (words.length == 1) {
        	button.setText(newEmoji + " " + words[0]);
        } else if (words.length == 2) {
            button.setText(newEmoji + " " + words[1]);
        } else {
        	throw handleInvalidButtonText(buttonText);
        }
    }

    public InvalidButtonTextException handleInvalidButtonText(String buttonText) {
        String errorMessage = String.format(
                "Invalid button text format '%s' for updating coin deletion button",
                buttonText
        );
        log.error(errorMessage);
        return new InvalidButtonTextException(errorMessage);
    }
}
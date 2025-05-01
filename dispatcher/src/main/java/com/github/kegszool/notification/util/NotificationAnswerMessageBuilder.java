package com.github.kegszool.notification.util;

import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;

import com.github.kegszool.localization.LocalizationService;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

@Component
public class NotificationAnswerMessageBuilder {

    private final LocalizationService localizationService;

    @Autowired
    public NotificationAnswerMessageBuilder(LocalizationService localizationService) {
        this.localizationService = localizationService;
    }

    public SendMessage createSuccessMessage() {
        return null;
    }

    public SendMessage createErrorMessage() {
        return null;
    }
}
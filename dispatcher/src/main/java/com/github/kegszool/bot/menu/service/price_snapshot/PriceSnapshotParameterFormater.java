package com.github.kegszool.bot.menu.service.price_snapshot;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class PriceSnapshotParameterFormater {

    @Value("${emoji_unicode_symbol.coin}")
    private String COIN_EMOJI_UNICODE_SYMBOL;

    public String formatTitle(String coinName, String parameterValue, String description) {
        String format = COIN_EMOJI_UNICODE_SYMBOL + " — <b>%s</b>\n\n%s — <b>%s</b>";
        return String.format(format, coinName, description, parameterValue);
    }
}
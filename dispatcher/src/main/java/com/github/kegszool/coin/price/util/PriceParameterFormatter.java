package com.github.kegszool.coin.price.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class PriceParameterFormatter {

    @Value("${emoji_unicode_symbol.coin}")
    private String COIN_EMOJI_UNICODE_SYMBOL;

    public String formatTitle(String coinName, String parameterValue, String description) {
        String format = COIN_EMOJI_UNICODE_SYMBOL + " — <b>%s</b>\n\n%s — <b>%s</b>";
        return String.format(format, coinName, description, parameterValue);
    }
}
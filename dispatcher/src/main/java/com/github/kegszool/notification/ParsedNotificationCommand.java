package com.github.kegszool.notification;

import java.math.BigDecimal;

public class ParsedNotificationCommand {

    private final String coin;
    private final BigDecimal percentage;

    public ParsedNotificationCommand(String coin, BigDecimal percentage) {
        this.coin = coin;
        this.percentage = percentage;
    }

    public String getCoin() {
        return coin;
    }

    public BigDecimal getPercentage() {
        return percentage;
    }
}
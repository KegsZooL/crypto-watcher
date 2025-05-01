package com.github.kegszool.notification.util;

import org.springframework.stereotype.Component;
import com.github.kegszool.notification.ParsedNotificationCommand;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class NotificationCommandParser {

    private static final Pattern PATTERN = Pattern.compile("/not (\\w{2,10}) ([+-]?\\d+(\\.\\d+)?%)");

    public Optional<ParsedNotificationCommand> parseIfValid(String command) {
        Matcher matcher = PATTERN.matcher(command.trim());
        if (matcher.matches()) {
            String coin = matcher.group(1);
            String percentStr = matcher.group(2).replace("%", "");
            try {
                BigDecimal percent = new BigDecimal(percentStr);
                return Optional.of(new ParsedNotificationCommand(coin, percent));
            } catch (NumberFormatException ignored) {}
        }
        return Optional.empty();
    }}
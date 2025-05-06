package com.github.kegszool.notification.util;

import java.util.Optional;
import java.math.BigDecimal;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Value;
import com.github.kegszool.notification.command.model.ParsedNotificationCommand;

@Component
public class NotificationCommandParser {

    private final Pattern pattern;

    public NotificationCommandParser(@Value("${menu.select_coin_notification.regex}")
                                     String commandRegex) {
        pattern = Pattern.compile(commandRegex);
    }

    public Optional<ParsedNotificationCommand> parseIfValid(String command) {
        Matcher matcher = pattern.matcher(command.trim());
        if (matcher.matches()) {
            String coin = matcher.group(1);
            String percentStr = matcher.group(2).replace("%", "");
            try {
                BigDecimal percent = new BigDecimal(percentStr);
                return Optional.of(new ParsedNotificationCommand(coin, percent));
            } catch (NumberFormatException ignored) {}
        }
        return Optional.empty();
    }
}
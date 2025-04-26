package com.github.kegszool.coin.addition.util;

import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class CoinCommandParser {

    public List<String> parse(String commandText) {
        if (commandText == null || commandText.isBlank()) return List.of();

        return Arrays.stream(commandText.trim().split("\\s+"))
                .skip(1)
                .map(String::toUpperCase)
                .distinct()
                .toList();
    }
}
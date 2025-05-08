package com.github.kegszool.coin;

import com.github.kegszool.exception.EntityNotFoundException;

public class CoinNotFoundException extends EntityNotFoundException {
    public CoinNotFoundException(String coinName) {
        super("Name: " + coinName);
    }
}
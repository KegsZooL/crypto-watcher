package com.github.kegszool.coin.deletion.service;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import com.github.kegszool.menu.service.MenuRegistry;
import com.github.kegszool.menu.service.MenuSelectionHandler;

import com.github.kegszool.messaging.util.MessageUtils;
import com.github.kegszool.coin.selection.util.state.MenuSelectionBuffer;
import com.github.kegszool.coin.selection.util.state.updater.CoinDeletionSelectionStateUpdater;

@Service
public class CoinDeletionSelectionHandler extends MenuSelectionHandler {

    @Autowired
    public CoinDeletionSelectionHandler(
            MenuSelectionBuffer menuSelectionBuffer,
            CoinDeletionSelectionStateUpdater dataUpdater,
            MenuRegistry menuRegistry,
            MessageUtils messageUtils
    ) {
        super(menuSelectionBuffer, dataUpdater, menuRegistry, messageUtils);
    }
}
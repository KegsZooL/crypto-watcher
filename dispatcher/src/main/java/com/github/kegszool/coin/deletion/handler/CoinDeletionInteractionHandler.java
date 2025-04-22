package com.github.kegszool.coin.deletion.handler;

import com.github.kegszool.menu.service.MenuRegistry;
import com.github.kegszool.menu.service.MenuSelectionHandler;

import com.github.kegszool.messaging.util.MessageUtils;
import com.github.kegszool.coin.selection.state.MenuSelectionBuffer;
import com.github.kegszool.coin.selection.state.updater.CoinSelectionStateUpdater;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

@Service
public class CoinDeletionInteractionHandler extends MenuSelectionHandler {

    @Autowired
    public CoinDeletionInteractionHandler(
            MenuSelectionBuffer menuSelectionBuffer,
            CoinSelectionStateUpdater dataUpdater,
            MenuRegistry menuRegistry,
            MessageUtils messageUtils
    ) {
        super(menuSelectionBuffer, dataUpdater, menuRegistry, messageUtils);
    }
}
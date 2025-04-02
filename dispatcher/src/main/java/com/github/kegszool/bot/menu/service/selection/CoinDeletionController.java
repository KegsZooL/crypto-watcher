package com.github.kegszool.bot.menu.service.selection;

import com.github.kegszool.bot.menu.service.managment.MenuRegistry;
import com.github.kegszool.bot.menu.service.selection.state_updater.CoinSelectionStateUpdater;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import com.github.kegszool.utils.MessageUtils;

@Service
public class CoinDeletionController extends AbstractSelectionController {

    @Autowired
    public CoinDeletionController(
            SelectionStateRepository selectionStateRepository,
            CoinSelectionStateUpdater dataUpdater,
            MenuRegistry menuRegistry,
            MessageUtils messageUtils
    ) {
        super(selectionStateRepository, dataUpdater, menuRegistry, messageUtils);
    }
}
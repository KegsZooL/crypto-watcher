package com.github.kegszool.bot.menu.service.selection.controller;

import com.github.kegszool.bot.menu.service.managment.MenuRegistry;
import com.github.kegszool.bot.menu.service.selection.SelectionStateRepository;
import com.github.kegszool.bot.menu.service.selection.data_updater.CoinDeletionDataUpdater;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import com.github.kegszool.utils.MessageUtils;

@Service
public class CoinDeletionController extends BaseSelectionController {

    @Autowired
    public CoinDeletionController(
            SelectionStateRepository selectionStateRepository,
            CoinDeletionDataUpdater dataUpdater,
            MenuRegistry menuRegistry,
            MessageUtils messageUtils
    ) {
        super(selectionStateRepository, dataUpdater, menuRegistry, messageUtils);
    }
}
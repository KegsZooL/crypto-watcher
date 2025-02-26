package com.github.kegszool.bot.menu.service.section.section_transfer.impl;

import com.github.kegszool.bot.menu.service.managment.MenuRegistry;
import com.github.kegszool.bot.menu.service.section.MenuSectionExtractor;
import com.github.kegszool.bot.menu.service.section.section_transfer.BaseMenuSectionTransfer;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class CoinDeletionSectionTransfer extends BaseMenuSectionTransfer {

    @Value("${menu.coin_selection.name}")
    private String COIN_SELECTIONS_MENU;

    @Value("${menu.coin_deletion_menu.name}")
    private String COIN_DELETION_MENU_NAME;

    @Value("${menu.coin_selection.prefix[0].coin}")
    private String COIN_PREFIX;

    @Value("${menu.coin_deletion_menu.prefix.unselected_coin_prefix}")
    private String UNSELECTED_DELETION_COIN_PREFIX;

    @Value("${emoji_unicode_symbol.ballot_box_with_check}")
    private String BALLOT_BOX_WITH_CHECK_UNICODE_SYMBOL;

    @Autowired
    public CoinDeletionSectionTransfer(MenuSectionExtractor sectionExtractor, MenuRegistry menuRegistry) {
        super(sectionExtractor, menuRegistry);
    }

    @PostConstruct
    private void init() {
        this.sourceMenuName = COIN_SELECTIONS_MENU;
        this.targetMenuName = COIN_DELETION_MENU_NAME;
        this.buttonFilter = button -> button.getCallbackData().startsWith(COIN_PREFIX);
        this.prefixButtonText = BALLOT_BOX_WITH_CHECK_UNICODE_SYMBOL + " ";
        this.prefixCallbackData = UNSELECTED_DELETION_COIN_PREFIX;
    }
}
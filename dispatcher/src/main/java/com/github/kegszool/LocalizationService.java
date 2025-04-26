package com.github.kegszool;

import lombok.Setter;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;

import java.util.Map;
import java.util.HashMap;

@Service
public class LocalizationService {

    @Setter
    private String currentLocale = "ru";

    private final Map<String, Map<String, String>> menuTitles = new HashMap<>();
    private final Map<String, Map<String, String>> sectionsConfig = new HashMap<>();
    private final Map<String, Map<String, String>> answerMessages = new HashMap<>();

    private final String MAIN_MENU_NAME;
    private final String MAIN_MENU_TITLE_RU;
    private final String MAIN_MENU_TITLE_EN;
    private final String MAIN_MENU_SECTIONS_RU;
    private final String MAIN_MENU_SECTIONS_EN;

    private final String COIN_SELECTION_MENU_NAME;
    private final String COIN_SELECTION_MENU_TITLE_RU;
    private final String COIN_SELECTION_MENU_TITLE_EN;
    private final String COIN_SELECTION_MENU_SECTIONS_RU;
    private final String COIN_SELECTION_MENU_SECTIONS_EN;
    private final String COIN_SELECTION_MENU_ANSWER_MESSAGE_RU;
    private final String COIN_SELECTION_MENU_ANSWER_MESSAGE_EN;

    private final String EDIT_COIN_SECTIONS_MENU_NAME;
    private final String EDIT_COIN_SECTIONS_MENU_TITLE_RU;
    private final String EDIT_COIN_SECTIONS_MENU_TITLE_EN;
    private final String EDIT_COIN_SECTIONS_MENU_SECTIONS_RU;
    private final String EDIT_COIN_SECTIONS_MENU_SECTIONS_EN;

    private final String COIN_DELETION_MENU_NAME;
    private final String COIN_DELETION_MENU_TITLE_RU;
    private final String COIN_DELETION_MENU_TITLE_EN;
    private final String COIN_DELETION_MENU_SECTIONS_RU;
    private final String COIN_DELETION_MENU_SECTIONS_EN;
    private final String COIN_DELETION_MENU_HINT_RU;
    private final String COIN_DELETION_MENU_HINT_EN;

    private final String COIN_ADDITION_MENU_NAME;
    private final String COIN_ADDITION_MENU_TITLE_RU;
    private final String COIN_ADDITION_MENU_TITLE_EN;
    private final String COIN_ADDITION_MENU_SECTIONS_RU;
    private final String COIN_ADDITION_MENU_SECTIONS_EN;
    private final String COIN_ADDITION_MENU_SUCCESS_MSG_RU;
    private final String COIN_ADDITION_MENU_SUCCESS_MSG_EN;
    private final String COIN_ADDITION_MENU_ERROR_MSG_RU;
    private final String COIN_ADDITION_MENU_ERROR_MSG_EN;

    private final String COIN_ADDITION_MENU_ALL_COINS_ADDED_RU;
    private final String COIN_ADDITION_MENU_ALL_COINS_ADDED_EN;

    private final String COIN_ADDITION_MENU_SOME_COINS_ADDED_RU;
    private final String COIN_ADDITION_MENU_SOME_COINS_ADDED_EN;

    private final String COIN_ADDITION_MENU_NO_COINS_ADDED_RU;
    private final String COIN_ADDITION_MENU_NO_COINS_ADDED_EN;

    private final String COIN_ADDITION_MENU_SUCCESS_MSG_TYPE;
	private final String COIN_ADDITION_MENU_ERROR_MSG_TYPE;

    private final String COIN_ADDITION_MENU_ALL_COINS_ADDED_MSG_TYPE;
    private final String COIN_ADDITION_MENU_SOME_COINS_ADDED_MSG_TYPE;
    private final String COIN_ADDITION_MENU_NO_COINS_ADDED_MSG_TYPE;

//    private final String PRICE_SNAPSHOT_MENU_NAME;
//    private final String PRICE_SNAPSHOT_MENU_TITLE_RU;
//    private final String PRICE_SNAPSHOT_MENU_TITLE_EN;
//    private final String PRICE_SNAPSHOT_MENU_SECTIONS_RU;
//    private final String PRICE_SNAPSHOT_MENU_SECTIONS_EN;

    public LocalizationService(
            @Value("${menu.main.name}") String MAIN_MENU_NAME,
            @Value("${menu.main.title.ru}") String MAIN_MENU_TITLE_RU,
            @Value("${menu.main.title.en}") String MAIN_MENU_TITLE_EN,
            @Value("${menu.main.sections.ru}") String MAIN_MENU_SECTIONS_RU,
            @Value("${menu.main.sections.en}") String MAIN_MENU_SECTIONS_EN,

            @Value("${menu.coin_selection.name}") String COIN_SELECTION_MENU_NAME,
            @Value("${menu.coin_selection.title.ru}") String COIN_SELECTION_MENU_TITLE_RU,
            @Value("${menu.coin_selection.title.en}") String COIN_SELECTION_MENU_TITLE_EN,
            @Value("${menu.coin_selection.sections.ru}") String COIN_SELECTION_MENU_SECTIONS_RU,
            @Value("${menu.coin_selection.sections.en}") String COIN_SELECTION_MENU_SECTIONS_EN,
            @Value("${menu.coin_selection.answer_message.ru}") String COIN_SELECTION_MENU_ANSWER_MESSAGE_RU,
            @Value("${menu.coin_selection.answer_message.en}") String COIN_SELECTION_MENU_ANSWER_MESSAGE_EN,

            @Value("${menu.edit_coin_sections.name}") String EDIT_COIN_SECTIONS_MENU_NAME,
            @Value("${menu.edit_coin_sections.title.ru}") String EDIT_COIN_SECTIONS_MENU_TITLE_RU,
            @Value("${menu.edit_coin_sections.title.en}") String EDIT_COIN_SECTIONS_MENU_TITLE_EN,
            @Value("${menu.edit_coin_sections.sections.ru}") String EDIT_COIN_SECTIONS_MENU_SECTIONS_RU,
            @Value("${menu.edit_coin_sections.sections.en}") String EDIT_COIN_SECTIONS_MENU_SECTIONS_EN,

            @Value("${menu.coin_deletion_menu.name}") String COIN_DELETION_MENU_NAME,
            @Value("${menu.coin_deletion_menu.title.ru}") String COIN_DELETION_MENU_TITLE_RU,
            @Value("${menu.coin_deletion_menu.title.en}") String COIN_DELETION_MENU_TITLE_EN,
            @Value("${menu.coin_deletion_menu.sections.ru}") String COIN_DELETION_MENU_SECTIONS_RU,
            @Value("${menu.coin_deletion_menu.sections.en}") String COIN_DELETION_MENU_SECTIONS_EN,
            @Value("${menu.coin_deletion_menu.hint.ru}") String COIN_DELETION_MENU_HINT_RU,
            @Value("${menu.coin_deletion_menu.hint.en}") String COIN_DELETION_MENU_HINT_EN,

            @Value("${menu.coin_addition.name}") String COIN_ADDITION_MENU_NAME,
            @Value("${menu.coin_addition.title.ru}") String COIN_ADDITION_MENU_TITLE_RU,
            @Value("${menu.coin_addition.title.en}") String COIN_ADDITION_MENU_TITLE_EN,
            @Value("${menu.coin_addition.sections.ru}") String COIN_ADDITION_MENU_SECTIONS_RU,
            @Value("${menu.coin_addition.sections.en}") String COIN_ADDITION_MENU_SECTIONS_EN,
            @Value("${menu.coin_addition.answer_messages.success.ru}") String COIN_ADDITION_MENU_SUCCESS_MSG_RU,
            @Value("${menu.coin_addition.answer_messages.success.en}") String COIN_ADDITION_MENU_SUCCESS_MSG_EN,
            @Value("${menu.coin_addition.answer_messages.error.ru}") String COIN_ADDITION_MENU_ERROR_MSG_RU,
            @Value("${menu.coin_addition.answer_messages.error.en}") String COIN_ADDITION_MENU_ERROR_MSG_EN,
            @Value("${menu.coin_addition.answer_messages.all_coins_added.ru}") String COIN_ADDITION_MENU_ALL_COINS_ADDED_RU,
            @Value("${menu.coin_addition.answer_messages.all_coins_added.en}") String COIN_ADDITION_MENU_ALL_COINS_ADDED_EN,
            @Value("${menu.coin_addition.answer_messages.some_coins_added.ru}") String COIN_ADDITION_MENU_SOME_COINS_ADDED_RU,
            @Value("${menu.coin_addition.answer_messages.some_coins_added.en}") String COIN_ADDITION_MENU_SOME_COINS_ADDED_EN,
            @Value("${menu.coin_addition.answer_messages.no_coins_added.ru}") String COIN_ADDITION_MENU_NO_COINS_ADDED_RU,
            @Value("${menu.coin_addition.answer_messages.no_coins_added.en}") String COIN_ADDITION_MENU_NO_COINS_ADDED_EN,
            @Value("${menu.coin_addition.answer_messages.success.msg_type}") String COIN_ADDITION_MENU_SUCCESS_MSG_TYPE,
            @Value("${menu.coin_addition.answer_messages.error.msg_type}") String COIN_ADDITION_MENU_ERROR_MSG_TYPE,
            @Value("${menu.coin_addition.answer_messages.all_coins_added.msg_type}") String COIN_ADDITION_MENU_ALL_COINS_ADDED_MSG_TYPE,
            @Value("${menu.coin_addition.answer_messages.some_coins_added.msg_type}") String COIN_ADDITION_MENU_SOME_COINS_ADDED_MSG_TYPE,
            @Value("${menu.coin_addition.answer_messages.no_coins_added.msg_type}") String COIN_ADDITION_MENU_NO_COINS_ADDED_MSG_TYPE
    ) {
        this.MAIN_MENU_NAME = MAIN_MENU_NAME;
        this.MAIN_MENU_TITLE_RU = MAIN_MENU_TITLE_RU;
        this.MAIN_MENU_TITLE_EN = MAIN_MENU_TITLE_EN;
        this.MAIN_MENU_SECTIONS_RU = MAIN_MENU_SECTIONS_RU;
        this.MAIN_MENU_SECTIONS_EN = MAIN_MENU_SECTIONS_EN;

        this.COIN_SELECTION_MENU_NAME = COIN_SELECTION_MENU_NAME;
        this.COIN_SELECTION_MENU_TITLE_RU = COIN_SELECTION_MENU_TITLE_RU;
        this.COIN_SELECTION_MENU_TITLE_EN = COIN_SELECTION_MENU_TITLE_EN;
        this.COIN_SELECTION_MENU_SECTIONS_RU = COIN_SELECTION_MENU_SECTIONS_RU;
        this.COIN_SELECTION_MENU_SECTIONS_EN = COIN_SELECTION_MENU_SECTIONS_EN;
        this.COIN_SELECTION_MENU_ANSWER_MESSAGE_RU = COIN_SELECTION_MENU_ANSWER_MESSAGE_RU;
        this.COIN_SELECTION_MENU_ANSWER_MESSAGE_EN = COIN_SELECTION_MENU_ANSWER_MESSAGE_EN;

        this.EDIT_COIN_SECTIONS_MENU_NAME = EDIT_COIN_SECTIONS_MENU_NAME;
        this.EDIT_COIN_SECTIONS_MENU_TITLE_RU = EDIT_COIN_SECTIONS_MENU_TITLE_RU;
        this.EDIT_COIN_SECTIONS_MENU_TITLE_EN = EDIT_COIN_SECTIONS_MENU_TITLE_EN;
        this.EDIT_COIN_SECTIONS_MENU_SECTIONS_RU = EDIT_COIN_SECTIONS_MENU_SECTIONS_RU;
        this.EDIT_COIN_SECTIONS_MENU_SECTIONS_EN = EDIT_COIN_SECTIONS_MENU_SECTIONS_EN;

        this.COIN_DELETION_MENU_NAME = COIN_DELETION_MENU_NAME;
        this.COIN_DELETION_MENU_TITLE_RU = COIN_DELETION_MENU_TITLE_RU;
        this.COIN_DELETION_MENU_TITLE_EN = COIN_DELETION_MENU_TITLE_EN;
        this.COIN_DELETION_MENU_SECTIONS_RU = COIN_DELETION_MENU_SECTIONS_RU;
        this.COIN_DELETION_MENU_SECTIONS_EN = COIN_DELETION_MENU_SECTIONS_EN;
        this.COIN_DELETION_MENU_HINT_RU = COIN_DELETION_MENU_HINT_RU;
        this.COIN_DELETION_MENU_HINT_EN = COIN_DELETION_MENU_HINT_EN;

        this.COIN_ADDITION_MENU_NAME = COIN_ADDITION_MENU_NAME;
        this.COIN_ADDITION_MENU_TITLE_RU = COIN_ADDITION_MENU_TITLE_RU;
        this.COIN_ADDITION_MENU_TITLE_EN = COIN_ADDITION_MENU_TITLE_EN;
        this.COIN_ADDITION_MENU_SECTIONS_RU = COIN_ADDITION_MENU_SECTIONS_RU;
        this.COIN_ADDITION_MENU_SECTIONS_EN = COIN_ADDITION_MENU_SECTIONS_EN;
        this.COIN_ADDITION_MENU_SUCCESS_MSG_RU = COIN_ADDITION_MENU_SUCCESS_MSG_RU;
        this.COIN_ADDITION_MENU_SUCCESS_MSG_EN = COIN_ADDITION_MENU_SUCCESS_MSG_EN;
        this.COIN_ADDITION_MENU_ERROR_MSG_RU = COIN_ADDITION_MENU_ERROR_MSG_RU;
        this.COIN_ADDITION_MENU_ERROR_MSG_EN = COIN_ADDITION_MENU_ERROR_MSG_EN;
        this.COIN_ADDITION_MENU_ALL_COINS_ADDED_RU = COIN_ADDITION_MENU_ALL_COINS_ADDED_RU;
        this.COIN_ADDITION_MENU_ALL_COINS_ADDED_EN = COIN_ADDITION_MENU_ALL_COINS_ADDED_EN;
        this.COIN_ADDITION_MENU_SOME_COINS_ADDED_RU = COIN_ADDITION_MENU_SOME_COINS_ADDED_RU;
        this.COIN_ADDITION_MENU_SOME_COINS_ADDED_EN = COIN_ADDITION_MENU_SOME_COINS_ADDED_EN;
        this.COIN_ADDITION_MENU_NO_COINS_ADDED_RU = COIN_ADDITION_MENU_NO_COINS_ADDED_RU;
        this.COIN_ADDITION_MENU_NO_COINS_ADDED_EN = COIN_ADDITION_MENU_NO_COINS_ADDED_EN;
        this.COIN_ADDITION_MENU_SUCCESS_MSG_TYPE = COIN_ADDITION_MENU_SUCCESS_MSG_TYPE;
        this.COIN_ADDITION_MENU_ERROR_MSG_TYPE = COIN_ADDITION_MENU_ERROR_MSG_TYPE;
        this.COIN_ADDITION_MENU_ALL_COINS_ADDED_MSG_TYPE = COIN_ADDITION_MENU_ALL_COINS_ADDED_MSG_TYPE;
        this.COIN_ADDITION_MENU_SOME_COINS_ADDED_MSG_TYPE = COIN_ADDITION_MENU_SOME_COINS_ADDED_MSG_TYPE;
        this.COIN_ADDITION_MENU_NO_COINS_ADDED_MSG_TYPE = COIN_ADDITION_MENU_NO_COINS_ADDED_MSG_TYPE;
    }

    @PostConstruct
    public void init() {
        initMainMenu();
        initCoinSelectionMenu();
        initEditCoinSectionsMenu();
        initCoinDeletionMenu();
        initCoinAdditionMenu();
        initPriceSnapshotMenu();
    }

    private void initMainMenu() {
        menuTitles.put(MAIN_MENU_NAME, Map.of(
                "ru", MAIN_MENU_TITLE_RU,
                "en", MAIN_MENU_TITLE_EN
        ));

        sectionsConfig.put(MAIN_MENU_NAME, Map.of(
                "ru", MAIN_MENU_SECTIONS_RU,
                "en", MAIN_MENU_SECTIONS_EN
        ));
    }

    private void initCoinSelectionMenu() {
        menuTitles.put(COIN_SELECTION_MENU_NAME, Map.of(
                "ru", COIN_SELECTION_MENU_TITLE_RU,
                "en", COIN_SELECTION_MENU_TITLE_EN
        ));

        sectionsConfig.put(COIN_SELECTION_MENU_NAME, Map.of(
                "ru", COIN_SELECTION_MENU_SECTIONS_RU,
                "en", COIN_SELECTION_MENU_SECTIONS_EN
        ));

        answerMessages.put(COIN_SELECTION_MENU_NAME, Map.of(
                "ru", COIN_SELECTION_MENU_ANSWER_MESSAGE_RU,
                "en", COIN_SELECTION_MENU_ANSWER_MESSAGE_EN
        ));
    }

    private void initEditCoinSectionsMenu() {
        menuTitles.put(EDIT_COIN_SECTIONS_MENU_NAME, Map.of(
                "ru", EDIT_COIN_SECTIONS_MENU_TITLE_RU,
                "en", EDIT_COIN_SECTIONS_MENU_TITLE_EN
        ));

        sectionsConfig.put(EDIT_COIN_SECTIONS_MENU_NAME, Map.of(
                "ru", EDIT_COIN_SECTIONS_MENU_SECTIONS_RU,
                "en", EDIT_COIN_SECTIONS_MENU_SECTIONS_EN
        ));
    }

    private void initCoinDeletionMenu() {
        menuTitles.put(COIN_DELETION_MENU_NAME, Map.of(
                "ru", COIN_DELETION_MENU_TITLE_RU,
                "en", COIN_DELETION_MENU_TITLE_EN
        ));

        answerMessages.put(COIN_DELETION_MENU_NAME + "_hint", Map.of(
                "ru", COIN_DELETION_MENU_HINT_RU,
                "en", COIN_DELETION_MENU_HINT_EN
        ));

        sectionsConfig.put(COIN_DELETION_MENU_NAME, Map.of(
                "ru", COIN_DELETION_MENU_SECTIONS_RU,
                "en", COIN_DELETION_MENU_SECTIONS_EN
        ));
    }

    private void initCoinAdditionMenu() {
        menuTitles.put(COIN_ADDITION_MENU_NAME, Map.of(
                "ru", COIN_ADDITION_MENU_TITLE_RU,
                "en", COIN_ADDITION_MENU_TITLE_EN
        ));

        sectionsConfig.put(COIN_ADDITION_MENU_NAME, Map.of(
                "ru", COIN_ADDITION_MENU_SECTIONS_RU,
                "en", COIN_ADDITION_MENU_SECTIONS_EN
        ));

        answerMessages.put(COIN_ADDITION_MENU_NAME + COIN_ADDITION_MENU_SUCCESS_MSG_TYPE, Map.of(
                "ru", COIN_ADDITION_MENU_SUCCESS_MSG_RU,
                "en", COIN_ADDITION_MENU_SUCCESS_MSG_EN
        ));

        answerMessages.put(COIN_ADDITION_MENU_NAME + COIN_ADDITION_MENU_ERROR_MSG_TYPE, Map.of(
                "ru", COIN_ADDITION_MENU_ERROR_MSG_RU,
                "en", COIN_ADDITION_MENU_ERROR_MSG_EN
        ));

        answerMessages.put(COIN_ADDITION_MENU_NAME + COIN_ADDITION_MENU_ALL_COINS_ADDED_MSG_TYPE, Map.of(
                "ru", COIN_ADDITION_MENU_ALL_COINS_ADDED_RU,
                "en", COIN_ADDITION_MENU_ALL_COINS_ADDED_EN
        ));

        answerMessages.put(COIN_ADDITION_MENU_NAME + COIN_ADDITION_MENU_SOME_COINS_ADDED_MSG_TYPE, Map.of(
                "ru", COIN_ADDITION_MENU_SOME_COINS_ADDED_RU,
                "en", COIN_ADDITION_MENU_SOME_COINS_ADDED_EN
        ));

        answerMessages.put(COIN_ADDITION_MENU_NAME + COIN_ADDITION_MENU_NO_COINS_ADDED_MSG_TYPE, Map.of(
                "ru", COIN_ADDITION_MENU_NO_COINS_ADDED_RU,
                "en", COIN_ADDITION_MENU_NO_COINS_ADDED_EN
        ));

    }

    private void initPriceSnapshotMenu() {

    }

    public String getAnswerMessage(String menuName) {
        return answerMessages.getOrDefault(menuName, Map.of())
                .getOrDefault(currentLocale, "Default answer msg");
    }

    public String getAnswerMessage(String menuName, String messageType) {
        return answerMessages.getOrDefault(menuName + "_" + messageType, Map.of())
                .getOrDefault(currentLocale, "Default answer msg");
    }

    public String getTitleText(String menuName) {
        return menuTitles.getOrDefault(menuName, Map.of())
                .getOrDefault(currentLocale, "Default message");
    }

    public String getSectionsConfig(String menuName) {
        return sectionsConfig.getOrDefault(menuName, Map.of())
                .getOrDefault(currentLocale, "Default section config");
    }
}
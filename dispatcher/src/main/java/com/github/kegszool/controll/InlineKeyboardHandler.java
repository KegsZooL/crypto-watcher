package com.github.kegszool.controll;

import com.github.kegszool.communication_service.RequestProducerService;
import com.github.kegszool.exception.UnknowRequestException;
import com.github.kegszool.menu.Menu;
import com.github.kegszool.menu.MenuNavigationService;
import com.github.kegszool.menu.MenuRegistry;
import com.github.kegszool.menu.impl.CoinExchangeRateMenu;
import com.github.kegszool.utils.MessageUtils;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

@Component
@Log4j2
public class InlineKeyboardHandler {

    private final MenuRegistry menuRegistry;
    private final MenuNavigationService navigationService;
    private final MessageUtils messageUtils;
    private final RequestProducerService requestService;

    @Value("${menu.actions.back}")
    private String BACK_COMMAND;

    @Value("${coin.prefix}")
    private String COIN_PREFIX;

    @Value("${spring.rabbitmq.queues.request_queue}")
    private String COIN_REQUEST_QUEUE;

    @Autowired
    public InlineKeyboardHandler(
            MenuRegistry menuRegistry,
            MenuNavigationService navigationService,
            MessageUtils messageUtils,
            RequestProducerService producerService
    ) {
        this.menuRegistry = menuRegistry;
        this.navigationService = navigationService;
        this.messageUtils = messageUtils;
        this.requestService = producerService;
    }


    public EditMessageText handleCallbackQuery(CallbackQuery query) {
        String request = query.getData();
        if(request.startsWith(COIN_PREFIX)) {

        }

    }

//    public EditMessageText handleCallbackQuery(CallbackQuery query) {
//        String request = query.getData();
//        if(request.startsWith(COIN_PREFIX)) {
//            return handleCoinPriceRequest(query);
//        }
//        return switch(request) {
//            case "exchange_rate_of_coins" -> handleCoinExchangeRateCommand(query);
//            case "settings" -> handleSettingsCommand(query);
//            case "alerts" -> handleAlertsCommand(query);
//            case "new_coin" -> handleAdditionOfNewCoin(query);
//            case BACK_COMMAND -> handleBackCommand(query);
//            default -> throw handleUnknowRequestException(query);
//        };
//    }
//
//    private EditMessageText handleBackCommand(CallbackQuery query) {
//        Long chatId = query.getMessage().getChatId();
//        String previousMenuName = navigationService.popMenu(chatId);
//        Menu previousMenu = menuRegistry.getMenu(previousMenuName);
//        return messageUtils.createEditMessageTextByCallbackQuery(
//                query,
//                previousMenu.getTitle(),
//                previousMenu.get()
//        );
//    }
//
//    private EditMessageText createResponseMenu(String request, CallbackQuery query)  {
//        Menu menu = menuRegistry.getMenu(request);
//        Long chatId = query.getMessage().getChatId();
//        navigationService.pushMenu(chatId, request);
//        return messageUtils.createEditMessageTextByCallbackQuery(
//                query,
//                menu.getTitle(),
//                menu.get()
//        );
//    }
//
//    private EditMessageText handleCoinPriceRequest(CallbackQuery query) {
//        String cryptocurrencyName = getCryptocurrencyNameByCallbackData(query);
//        log.info("The process of getting the price of a coin: '{}'", cryptocurrencyName);
//        requestService.produce(COIN_REQUEST_QUEUE, cryptocurrencyName);
//        String title = String.format("Вы выбрали монету: %s", cryptocurrencyName);
//        return messageUtils.createEditMessageTextByCallbackQuery(query, title);
//    }
//
//    private String getCryptocurrencyNameByCallbackData(CallbackQuery query) {
//        String data = query.getData();
//        int lengthOfCoinPrefix = COIN_PREFIX.length();
//        int lengthOfData = data.length();
//        return data.substring(lengthOfCoinPrefix, lengthOfData);
//    }
//
//    private EditMessageText handleCoinExchangeRateCommand(CallbackQuery query) {
//        log.info("Processing the receipt of the coin exchange rete menu");
//        var keyboard = coinExchangeRateMenu.get();
//        String title = coinExchangeRateMenu.getTitle();
////        return messageUtils.createEditMessageTextByCallbackQuery(query, title, keyboard);
//        return createResponseMenu(, query);
//    }
//
//    private EditMessageText handleSettingsCommand(CallbackQuery query) {
//        return null;
//    }
//
//    private EditMessageText handleAlertsCommand(CallbackQuery query) {
//        return null;
//    }
//
//    private EditMessageText handleAdditionOfNewCoin(CallbackQuery query) {
//        return null;
//    }
//
//    private UnknowRequestException handleUnknowRequestException(CallbackQuery query) {
//        String request = query.getData();
//        Long chatId = query.getMessage().getChatId();
//        String errorMessage = String.format("Unknow request detected: %s. Chat id: %d", request, chatId);
//        log.error(errorMessage);
//        throw new UnknowRequestException(errorMessage);
//    }
}
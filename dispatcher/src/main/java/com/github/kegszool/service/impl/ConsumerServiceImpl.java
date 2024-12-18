package com.github.kegszool.service.impl;

import com.github.kegszool.controll.TelegramBotController;
import com.github.kegszool.service.ConsumerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.botapimethods.PartialBotApiMethod;

import java.io.Serializable;

@Service
public class ConsumerServiceImpl implements ConsumerService {


    @Override
    public <T extends Serializable, Method extends PartialBotApiMethod<T>> void consume(Method message) {

    }
}

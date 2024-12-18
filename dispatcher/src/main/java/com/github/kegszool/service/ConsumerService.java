package com.github.kegszool.service;

import org.telegram.telegrambots.meta.api.methods.botapimethods.PartialBotApiMethod;
import java.io.Serializable;

public interface ConsumerService {
    <T extends Serializable, Method extends PartialBotApiMethod<T>> void consume(Method message);
}
package com.github.kegszool.websocket.checker;

import java.math.BigDecimal;

public interface NotificationCheckerService {
    void check(String instId, BigDecimal price);
}
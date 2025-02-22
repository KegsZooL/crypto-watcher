package com.github.kegszool.bot.menu.service.transfer;

import java.util.List;
import lombok.extern.log4j.Log4j2;

import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;

import com.github.kegszool.exception.bot.MenuSectionTransferNotFoundException;

@Log4j2
@Component
public class MenuSectionTransferService {

    private final List<BaseMenuSectionTransfer> transfers;

    @Autowired
    public MenuSectionTransferService(List<BaseMenuSectionTransfer> transfers) {
        this.transfers = transfers;
    }

    public void performTransfer(Class<? extends BaseMenuSectionTransfer> transferClass) {
        transfers.stream()
                .filter(transfer -> transfer.getClass().equals(transferClass))
                .findFirst()
                .ifPresentOrElse(
                        BaseMenuSectionTransfer::perform,
                        () -> { throw hanleMenuSectionTransferNotFoundException(transferClass); }
                );
    }

    private MenuSectionTransferNotFoundException hanleMenuSectionTransferNotFoundException(
            Class<? extends BaseMenuSectionTransfer> nonExistentTransferClass
    ) {
        String className = nonExistentTransferClass.getSimpleName();
        log.error("Menu section transfer was not found. Non-existent transfer: \"{}\"", className);
        throw new MenuSectionTransferNotFoundException(className);
    }
}
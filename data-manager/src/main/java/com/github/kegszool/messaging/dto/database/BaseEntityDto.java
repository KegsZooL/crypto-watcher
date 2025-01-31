package com.github.kegszool.messaging.dto.database;

public abstract class BaseEntityDto {

    protected int id;

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return this.id;
    }
}
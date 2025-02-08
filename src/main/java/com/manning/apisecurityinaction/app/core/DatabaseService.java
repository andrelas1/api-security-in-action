package com.manning.apisecurityinaction.app.core;

import org.dalesbred.Database;

public class DatabaseService {

    private final Database db;

    public DatabaseService(String url, String username, String password) {
        this.db = Database.forUrlAndCredentials(url, username, password);
    }

    public Database getDb() {
        return db;
    }
}

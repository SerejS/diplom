package com.serejs.diplom.desktop.server;

import com.serejs.diplom.desktop.utils.Encrypt;

public class User {
    private final String username;
    private final String password;

    public User(String username, String password) throws Exception {
        this.username = username;
        this.password = Encrypt.encrypt(username, password);
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}

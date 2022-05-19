package com.serejs.diplom.desktop.server.controllers;

import com.serejs.diplom.desktop.server.User;
import com.serejs.diplom.desktop.utils.Encrypt;
import org.junit.Assert;
import org.junit.Test;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

public class TestUserClientController {
    private final String rightUsername = System.getenv("rightUsername");
    private final String rightPassword = System.getenv("rightPassword");
    private final String wrongUsername = System.getenv("wrongUsername");
    private final String wrongPassword = System.getenv("wrongPassword");

    @Test
    public void testLogin() {
        try {
            UserClientController.auth(wrongUsername, wrongPassword);
            throw new AssertionError("Успешная аутентификация с неверными данными");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testLogin2() {
        try {
            UserClientController.auth(rightUsername, rightPassword);
        } catch (Exception e) {
            e.printStackTrace();
            throw new AssertionError("Ошибка аутентификации c верными данными");
        }
    }

    @Test
    public void testLogin3() {
        try {
            UserClientController.auth(rightUsername, wrongPassword);
            throw new AssertionError("Успешная аутентификация с неверными данными");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testLogin4() {
        try {
            UserClientController.auth(wrongUsername, rightPassword);
            throw new AssertionError("Успешная аутентификация с неверными данными");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testHashing() {
        var username = "name";
        var password = "pass";

        User user = null;
        try {
            user = new User(username, password);
        } catch (Exception e) {
            e.printStackTrace();
        }

        Assert.assertNotNull(user);

        Assert.assertEquals(username, user.getUsername());
        Assert.assertNotEquals(password, user.getPassword());

        String encrypted = null;
        try {
            encrypted = Encrypt.encrypt(username, password);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            e.printStackTrace();
        }

        Assert.assertNotNull(encrypted);
        Assert.assertEquals(encrypted, user.getPassword());
    }
}

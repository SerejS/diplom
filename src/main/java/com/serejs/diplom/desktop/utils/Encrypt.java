package com.serejs.diplom.desktop.utils;

import javafx.util.Pair;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;

public class Encrypt {
    public static Pair<String, String> encrypt(String pass) throws NoSuchAlgorithmException, InvalidKeySpecException {
        var salt = new byte[8];
        SecureRandom.getInstance("SHA1PRNG").nextBytes(salt);

        var passChars = pass.toCharArray();

        var spec = new PBEKeySpec(passChars, salt, 13, 128);

        var factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA512");
        var resultPass = factory.generateSecret(spec).getEncoded();

        var encoder = Base64.getEncoder();
        return new Pair<>(encoder.encodeToString(resultPass), encoder.encodeToString(salt));
    }
}

package com.serejs.diplom.desktop.utils;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;

public class Encrypt {
    public static String encrypt(String username, String pass) throws NoSuchAlgorithmException, InvalidKeySpecException {
        var salt = username.getBytes(StandardCharsets.UTF_8);

        var passChars = pass.toCharArray();

        var spec = new PBEKeySpec(passChars, salt, 13, 128);

        var factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA512");
        var resultPass = factory.generateSecret(spec).getEncoded();

        var encoder = Base64.getEncoder();
        return encoder.encodeToString(resultPass);
    }
}

package com.sid.securevault.utils;

import org.bouncycastle.crypto.generators.BCrypt;

import java.security.SecureRandom;
import java.util.Base64;

public class PasswordManager {

    public static String encrypt(String password) {
        byte[] salt = generateSalt();
        byte[] hashedPassword = BCrypt.generate(password.getBytes(), salt, Constants.COST_FACTOR);
        return Base64.getEncoder().encodeToString(salt)
                .concat(Constants.DOLLAR)
                .concat(Base64.getEncoder().encodeToString(hashedPassword));
    }

    public static boolean verifyPassword(String hash, String password) {
        String[] parts = hash.split("\\".concat(Constants.DOLLAR));
        byte[] salt = Base64.getDecoder().decode(parts[0]);
        byte[] hashedPassword = Base64.getDecoder().decode(parts[1]);
        byte[] hashedPasswordToCheck = BCrypt.generate(password.getBytes(), salt, Constants.COST_FACTOR);
        return Base64.getEncoder().encodeToString(hashedPasswordToCheck).equals(Base64.getEncoder().encodeToString(hashedPassword));
    }

    private static byte[] generateSalt() {
        byte[] salt = new byte[16];
        new SecureRandom().nextBytes(salt);
        return salt;
    }

}

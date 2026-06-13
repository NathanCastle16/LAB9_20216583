package com.example.lab9_20216583.util;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class PasswordUtil {

    private PasswordUtil() {
    }

    public static String sha256(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(password.getBytes(StandardCharsets.UTF_8));
            StringBuilder hexadecimal = new StringBuilder();

            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexadecimal.append('0');
                }
                hexadecimal.append(hex);
            }

            return hexadecimal.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("No se pudo aplicar SHA-256", e);
        }
    }
}

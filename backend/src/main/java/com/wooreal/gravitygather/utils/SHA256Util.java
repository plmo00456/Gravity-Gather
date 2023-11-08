package com.wooreal.gravitygather.utils;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

public class SHA256Util {
    private static final String SHA_256 = "SHA-256";

    public static String generateHashWithSalt(String message, String salt) {
        return getSHA256(message + salt);
    }

    public static String generateSalt() {
        SecureRandom secureRandom = new SecureRandom();
        byte[] salt = new byte[16];
        secureRandom.nextBytes(salt);
        return convertByteToHex(salt);
    }

    private static String getSHA256(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance(SHA_256);
            byte[] messageDigest = md.digest(input.getBytes());
            return convertByteToHex(messageDigest);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    private static String convertByteToHex(byte[] byteData) {
        StringBuilder sb = new StringBuilder();
        for (byte datum : byteData) {
            sb.append(Integer.toString((datum & 0xff) + 0x100, 16).substring(1));
        }
        return sb.toString();
    }
}

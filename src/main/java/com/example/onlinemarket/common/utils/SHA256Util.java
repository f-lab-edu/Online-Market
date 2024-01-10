package com.example.onlinemarket.common.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class SHA256Util {
    private static final String ENCRYPTION_TYPE = "SHA-256";

    public static String encryptSHA256(String str) {

        String encryptedString = null;

        try {
            StringBuilder sb = new StringBuilder();
            MessageDigest sh = MessageDigest.getInstance(ENCRYPTION_TYPE);

            sh.update(str.getBytes());
            byte[] byteData = sh.digest();
            for (int i = 0; i < byteData.length; i++) {
                sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
            }
            encryptedString = sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("암호화 에러!", e);
        }
        return encryptedString;
    }
}

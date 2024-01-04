package com.example.onlinemarket.common.utils;

public class SHA256PasswordEncoder implements PasswordEncoder {

    @Override
    public String encryptSHA256(String str) {
        return SHA256Util.encryptSHA256(str);
    }
}
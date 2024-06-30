package com.example.onlinemarket.common.utils;

public interface PasswordEncryptor {

    String encode(String password);

    boolean isMatch(String password, String hashedPassword);
}

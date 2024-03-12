package com.example.onlinemarket.common.utils;

public interface PasswordEncryptor {

    String encrypt(String password);

    boolean isMatch(String password, String hashedPassword);
}

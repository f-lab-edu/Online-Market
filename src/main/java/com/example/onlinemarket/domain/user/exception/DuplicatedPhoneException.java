package com.example.onlinemarket.domain.user.exception;

public class DuplicatedPhoneException extends RuntimeException {

    public DuplicatedPhoneException(String message) {
        super(message);
    }
}

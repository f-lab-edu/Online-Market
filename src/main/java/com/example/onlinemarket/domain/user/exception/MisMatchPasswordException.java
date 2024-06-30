package com.example.onlinemarket.domain.user.exception;

public class MisMatchPasswordException extends RuntimeException {

    public MisMatchPasswordException(String message) {
        super(message);
    }
}

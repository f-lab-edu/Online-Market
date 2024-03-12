package com.example.onlinemarket.domain.user.exception;

public class PasswordMisMatchException extends RuntimeException {

    public PasswordMisMatchException(String message) {
        super(message);
    }

    public PasswordMisMatchException() {
        super("비밀번호가 일치하지 않습니다.");
    }
}

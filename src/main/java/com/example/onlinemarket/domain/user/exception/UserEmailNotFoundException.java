package com.example.onlinemarket.domain.user.exception;

public class UserEmailNotFoundException extends RuntimeException {

    public UserEmailNotFoundException(String message) {
        super(message);
    }

    public UserEmailNotFoundException() {
        super("사용자 이메일이 존재하지 않습니다.");
    }
}

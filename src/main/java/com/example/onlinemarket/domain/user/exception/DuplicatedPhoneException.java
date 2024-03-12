package com.example.onlinemarket.domain.user.exception;

public class DuplicatedPhoneException extends RuntimeException {

    public DuplicatedPhoneException(String message) {
        super(message);
    }

    public DuplicatedPhoneException() {
        super("이미 존재하는 휴대폰 번호입니다.");
    }
}

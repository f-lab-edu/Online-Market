package com.example.onlinemarket.domain.user.exception;

public class DuplicatedEmailException extends RuntimeException {

    public DuplicatedEmailException(String message) {
        super(message);
    }

    public DuplicatedEmailException() {
        super("이미 존재하는 이메일입니다.");
    }
}

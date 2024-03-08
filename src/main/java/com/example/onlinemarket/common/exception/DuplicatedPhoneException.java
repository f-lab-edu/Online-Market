package com.example.onlinemarket.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)

public class DuplicatedPhoneException extends RuntimeException {

	public DuplicatedPhoneException(String message) {
		super(message);
	}
}

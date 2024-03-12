package com.example.onlinemarket.common.exception;


import com.example.onlinemarket.domain.user.exception.DuplicatedEmailException;
import com.example.onlinemarket.domain.user.exception.DuplicatedPhoneException;
import com.example.onlinemarket.domain.user.exception.PasswordMisMatchException;
import com.example.onlinemarket.domain.user.exception.UnauthorizedUserException;
import com.example.onlinemarket.domain.user.exception.UserEmailNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({
        DuplicatedEmailException.class,
        DuplicatedPhoneException.class
    })
    public ResponseEntity<String> handleDuplicatedExceptions(RuntimeException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(UserEmailNotFoundException.class)
    public ResponseEntity<String> handleUserEmailNotFoundException(UserEmailNotFoundException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(PasswordMisMatchException.class)
    public ResponseEntity<String> handlePasswordMisMatchException(PasswordMisMatchException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UnauthorizedUserException.class)
    public ResponseEntity<String> handleUnauthorizedUserException(UnauthorizedUserException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
    }
}

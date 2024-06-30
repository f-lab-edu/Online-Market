package com.example.onlinemarket.common.exception;


import com.example.onlinemarket.domain.category.exception.DuplicatedCategoryNameException;
import com.example.onlinemarket.domain.category.exception.NotFoundCategoryException;
import com.example.onlinemarket.domain.order.exception.OrderQuantityExceedsStockException;
import com.example.onlinemarket.domain.product.exception.NoProductQuantityException;
import com.example.onlinemarket.domain.product.exception.NotFoundProductException;
import com.example.onlinemarket.domain.user.exception.DuplicatedPhoneException;
import com.example.onlinemarket.domain.user.exception.DuplicatedUserIdException;
import com.example.onlinemarket.domain.user.exception.MisMatchPasswordException;
import com.example.onlinemarket.domain.user.exception.NotFoundUserIdException;
import com.example.onlinemarket.domain.user.exception.UnauthorizedUserException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({
        DuplicatedUserIdException.class,
        DuplicatedPhoneException.class
    })
    public ResponseEntity<String> handleUserDomainDuplicatedExceptions(RuntimeException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(NotFoundUserIdException.class)
    public ResponseEntity<String> handleNotFoundUserIdException(NotFoundUserIdException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(MisMatchPasswordException.class)
    public ResponseEntity<String> handleMisMatchPasswordException(MisMatchPasswordException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UnauthorizedUserException.class)
    public ResponseEntity<String> handleUnauthorizedUserException(UnauthorizedUserException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(NotFoundProductException.class)
    public ResponseEntity<String> handleNotFoundProductException(NotFoundProductException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(NoProductQuantityException.class)
    public ResponseEntity<String> handleNoProductQuantityException(NoProductQuantityException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(OrderQuantityExceedsStockException.class)
    public ResponseEntity<String> handleOrderQuantityExceedsStockException(OrderQuantityExceedsStockException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NotFoundCategoryException.class)
    public ResponseEntity<String> handleNotFoundCategoryException(NotFoundCategoryException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(DuplicatedCategoryNameException.class)
    public ResponseEntity<String> handleDuplicatedCategoryNameException(DuplicatedCategoryNameException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
    }
}

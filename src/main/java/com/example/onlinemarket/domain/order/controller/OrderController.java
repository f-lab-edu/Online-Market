package com.example.onlinemarket.domain.order.controller;

import com.example.onlinemarket.domain.order.dto.request.OrderRequest;
import com.example.onlinemarket.domain.order.service.OrderService;
import com.example.onlinemarket.domain.user.constants.SessionKey;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttribute;

@RequestMapping("/orders")
@RestController
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<Long> createOrder(@SessionAttribute(name = SessionKey.LOGGED_IN_USER) Long userId, @RequestBody @Valid OrderRequest request) {
        Long orderId = orderService.createOrder(userId, request);
        return new ResponseEntity<>(orderId, HttpStatus.CREATED);
    }
}

package com.example.onlinemarket.domain.user.controller;

import com.example.onlinemarket.domain.user.dto.UserDTO;
import com.example.onlinemarket.domain.user.service.LoginService;
import com.example.onlinemarket.domain.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/users")
@RequiredArgsConstructor
@RestController
public class UserController {

    private final UserService userService;
    private final LoginService loginService;

    @PostMapping("/sign-up")
    public ResponseEntity<Void> signUp(@RequestBody @Valid UserDTO userDTO) {
        userService.signUp(userDTO);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}
package com.example.onlinemarket.domain.user.controller;

import com.example.onlinemarket.domain.user.dto.LoginRequest;
import com.example.onlinemarket.domain.user.dto.SignUpRequest;
import com.example.onlinemarket.domain.user.entity.User;
import com.example.onlinemarket.domain.user.service.LoginService;
import com.example.onlinemarket.domain.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@RestController
public class UserController {

    private final UserService userService;
    private final LoginService loginService;

    @PostMapping
    public ResponseEntity<Void> signUp(@RequestBody @Valid SignUpRequest signUpRequest) {
        userService.signUp(signUpRequest);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping("/check-duplication")
    public ResponseEntity<Void> checkUserEmailDuplication(@RequestParam String userEmail) {
        userService.checkUserEmailDuplication(userEmail);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity<Void> login(@RequestBody @Valid LoginRequest loginRequest) {
        User user = userService.findLoggedInUser(loginRequest);
        loginService.login(user.getEmail());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout() {
        loginService.logout();
        return new ResponseEntity<>(HttpStatus.OK);
    }
}

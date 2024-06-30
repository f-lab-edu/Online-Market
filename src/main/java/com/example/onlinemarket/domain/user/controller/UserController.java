package com.example.onlinemarket.domain.user.controller;

import com.example.onlinemarket.domain.user.dto.UserDto;
import com.example.onlinemarket.domain.user.dto.request.LoginRequest;
import com.example.onlinemarket.domain.user.dto.request.SignUpRequest;
import com.example.onlinemarket.domain.user.service.LoginService;
import com.example.onlinemarket.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/users")
@RequiredArgsConstructor
@RestController
public class UserController {

    private final UserService userService;
    private final LoginService loginService;

    @PostMapping
    public ResponseEntity<Void> signUp(@RequestBody @Validated SignUpRequest request) {
        userService.signUp(request);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping("/check-duplication")
    public ResponseEntity<Void> checkUserEmailDuplication(@RequestParam String userId) {
        userService.checkUserIdDuplication(userId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity<Void> login(@RequestBody @Validated LoginRequest request) {
        UserDto userDto = userService.findLoggedInUser(request);
        loginService.login(userDto.getId());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout() {
        loginService.logout();
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}

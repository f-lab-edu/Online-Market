package com.example.onlinemarket.domain.user.service;

import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public interface LoginService {

    void login(String email);

    void logout();

    Optional<String> getLoginUserEmail();

}

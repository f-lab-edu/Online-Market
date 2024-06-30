package com.example.onlinemarket.domain.user.service;

import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public interface LoginService {

    void login(long id);

    void logout();

    Optional<Long> getLoginUserId();
}

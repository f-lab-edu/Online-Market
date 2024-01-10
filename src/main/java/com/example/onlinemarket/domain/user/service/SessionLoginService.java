package com.example.onlinemarket.domain.user.service;

import com.example.onlinemarket.domain.user.constants.SessionKey;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SessionLoginService implements LoginService {

    private final HttpSession session;

    @Override
    public void login(int id) {

        session.setAttribute(SessionKey.LOGGED_IN_USER, id);
    }
}
